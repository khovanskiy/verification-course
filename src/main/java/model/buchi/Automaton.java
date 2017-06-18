package model.buchi;

import java.util.*;

/**
 * An implementation of the nondeterministic Buchi automata. States here are just numbers from 0 to n - 1,
 * where n - the size of the automaton. State 0 is the initial state.
 *
 * @param <T> type of the alphabet
 */
public class Automaton<T> {
    private final List<Map<T, List<Integer>>> automaton;
    private final Set<Integer> acceptingSet;
    private int initialState;

    public Automaton(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        automaton = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            automaton.add(new LinkedHashMap<>());
        }
        acceptingSet = new HashSet<>();
    }

    public List<Map<T, List<Integer>>> getAutomaton() {
        return automaton;
    }

    public Set<Integer> getAcceptingSet() {
        return acceptingSet;
    }

    public int getInitialState() {
        return initialState;
    }

    public void setInitialState(int state) {
        checkState(state);
        initialState = state;
    }

    private void checkState(int state) {
        if (state < 0 || state >= size()) {
            throw new IllegalArgumentException();
        }
    }

    public int size() {
        return automaton.size();
    }

    public boolean isAccepting(int state) {
        return acceptingSet.contains(state);
    }

    public void addTransition(int stateA, int stateB, T symbol) {
        checkState(stateA);
        checkState(stateB);
        Map<T, List<Integer>> outgoings = automaton.get(stateA);
        if (!outgoings.containsKey(symbol)) {
            outgoings.put(symbol, new ArrayList<>());
        }
        outgoings.get(symbol).add(stateB);
    }

    public void setAccepting(int state) {
        checkState(state);
        acceptingSet.add(state);
    }

    public Collection<T> findAWord() {
        int n = size();
        int[] color1 = new int[n];
        int[] color2 = new int[n];
        Deque<T> path = new ArrayDeque<>();
        dfs1(0, color1, color2, path);
        return path;
    }

    private boolean dfs1(int v, int[] color1, int[] color2, Deque<T> path) {
        color1[v] = 1;
        for (T symbol : automaton.get(v).keySet()) {
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
        if (isAccepting(v)) {
            if (dfs2(v, color1, color2, path)) {
                return true;
            }
        }
        color1[v] = 2;
        return false;
    }

    private boolean dfs2(int v, int[] color1, int[] color2, Deque<T> path) {
        color2[v] = 1;
        for (T symbol : automaton.get(v).keySet()) {
            for (int u : automaton.get(v).get(symbol)) {
                if (color1[u] == 1) {
                    path.addLast(symbol);
                    return true;
                }
                if (color2[u] == 0) {
                    path.addLast(symbol);
                    if (dfs2(u, color1, color2, path)) {
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
