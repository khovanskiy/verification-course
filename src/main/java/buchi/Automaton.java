package buchi;

import java.util.*;

/**
 * An implementation of the nondeterministic Buchi automata. States here are just numbers from 0 to n - 1,
 * where n - the size of the automaton. State 0 is the initial state.
 * @param <T> type of the alphabet
 */
public class Automaton<T> {
    private List<Map<T, List<Integer>>> automaton;
    private Set<Integer> acceptingSet;
    private int initialState;

    public Automaton(int n){
        if(n < 1){
            throw new IllegalArgumentException();
        }
        automaton = new ArrayList<>();
        for(int i = 0; i < n; i++){
            automaton.add(new LinkedHashMap<>());
        }
        acceptingSet = new HashSet<>();
    }

    public int size(){
        return automaton.size();
    }

    public boolean isAccepting(int state){
        return acceptingSet.contains(state);
    }

    public void addTransition(int stateA, int stateB, T symbol){
        checkState(stateA);
        checkState(stateB);
        Map<T, List<Integer>> outgoings = automaton.get(stateA);
        if(!outgoings.containsKey(symbol)){
            outgoings.put(symbol, new ArrayList<>());
        }
        outgoings.get(symbol).add(stateB);
    }

    public void setInitialState(int state){
        checkState(state);
        initialState = state;
    }

    public void setAccepting(int state){
        checkState(state);
        acceptingSet.add(state);
    }

    private static int convert(int stateA, int stateB, int flag, int sizeB){
        return 2 * stateA * sizeB + 2 * stateB + flag;
    }

    private void checkState(int state){
        if(state < 0 || state >= size()){
            throw new IllegalArgumentException();
        }
    }

    public static <T> Automaton<T> intersect(Automaton<T> a, Automaton<T> b){
        int n = a.size();
        int m = b.size();
        int size = n * m * 2;
        Automaton<T> c = new Automaton<>(size);
        // Build all transitions
        for(int i = 0; i < n; i++){
            Map<T, List<Integer>> iState = a.automaton.get(i);
            for(int j = 0; j < m; j++){
                Map<T, List<Integer>> jState = b.automaton.get(j);
                Set<T> commonEdges = new LinkedHashSet<>(iState.keySet());
                commonEdges.retainAll(jState.keySet());
                for(T t: commonEdges){
                    for(int is: iState.get(t)){
                        for(int js : jState.get(t)){
                            c.addTransition(convert(i, j, 0, m), convert(is, js, a.isAccepting(i) ? 1 : 0, m), t);
                            c.addTransition(convert(i, j, 1, m), convert(is, js, a.isAccepting(j) ? 0 : 1, m), t);
                        }
                    }
                }
            }
        }
        // Accepting set of the new automaton
        for(int accB : b.acceptingSet){
            for(int i = 0; i < n; i++){
                c.setAccepting(convert(i, accB, 1, m));
            }
        }
        c.setInitialState(convert(a.initialState, b.initialState, 0, m));
        return c;
    }

    public Collection<T> findAWord(){
        int n = size();
        int[] color1 = new int[n];
        int[] color2 = new int[n];
        Deque<T> path = new ArrayDeque<>();
        dfs1(0, color1, color2, path);
        return path;
    }

    private boolean dfs1(int v, int[] color1, int[] color2, Deque<T> path) {
        color1[v] = 1;
        for(T symbol : automaton.get(v).keySet()) {
            for (int u : automaton.get(v).get(symbol)) {
                if (color1[u] == 0) {
                    path.addLast(symbol);
                    if (dfs1(u, color1, color2, path)) {
                        return true;
                    }
                    path.removeLast();
                }
            }
        }
        if(isAccepting(v)){
            if(dfs2(v, color1, color2, path)){
                return true;
            }
        }
        color1[v] = 2;
        return false;
    }

    private boolean dfs2(int v, int[] color1, int[] color2, Deque<T> path) {
        color2[v] = 1;
        for(T symbol : automaton.get(v).keySet()){
            for(int u : automaton.get(v).get(symbol)){
                if(color1[u] == 1){
                    path.addLast(symbol);
                    return true;
                }
                if(color2[u] == 0){
                    path.addLast(symbol);
                    if(dfs2(u, color1, color2, path)){
                        return true;
                    }
                    path.removeLast();
                }
            }
        }
        color2[v] = 2;
        return false;
    }
}
