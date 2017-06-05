package buchi;

import java.util.*;

public class Automaton<T> {
    private List<Map<T, List<Integer>>> automaton;
    private Set<Integer> acceptingSet;

    public Automaton(int n){
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
        Map<T, List<Integer>> outgoings = automaton.get(stateA);
        if(!outgoings.containsKey(symbol)){
            outgoings.put(symbol, new ArrayList<>());
        }
        outgoings.get(symbol).add(stateB);
    }

    public void setAccepting(int state){
        acceptingSet.add(state);
    }

    private static int convert(int stateA, int stateB, int flag, int sizeB){
        return 2 * stateA * sizeB + 2 * stateB + flag;
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
        return c;
    }

    public Iterator<T> findAWord(){
        
    }
}
