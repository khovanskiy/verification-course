package model.buchi;

import java.util.*;

/**
 * An implementation of the nondeterministic Buchi automata. States here are just numbers from 0 to n - 1,
 * where n - the size of the automaton. State 0 is the initial state.
 *
 * @param <T> type of the alphabet
 */
public class Automaton<T> {
    private static final Integer WHITE = null;
    private static final Integer GRAY = 1;
    private static final Integer BLACK = 2;
    private final Map<Integer, Map<T, List<Integer>>> automaton = new HashMap<>();
    private final Set<Integer> acceptingSet;
    private int initialState;

    public Automaton(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        acceptingSet = new HashSet<>();
    }

    public Map<Integer, Map<T, List<Integer>>> getAutomaton() {
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
        /*if (state < 0 || state >= size()) {
            throw new IllegalArgumentException();
        }*/
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
        Map<T, List<Integer>> outgoings = get(stateA);
        if (!outgoings.containsKey(symbol)) {
            outgoings.put(symbol, new ArrayList<>());
        }
        outgoings.get(symbol).add(stateB);
    }

    public Map<T, List<Integer>> get(int stateId) {
        return automaton.computeIfAbsent(stateId, k -> new LinkedHashMap<>());
    }

    public void setAccepting(int state) {
        checkState(state);
        acceptingSet.add(state);
    }

    public Collection<T> findAWord() {
        int n = size();
        Map<Integer, Integer> color1 = new HashMap<>();
        Map<Integer, Integer> color2 = new HashMap<>();
        Deque<T> path = new ArrayDeque<>();
        dfs1(0, color1, color2, path);
        return path;
    }

    private boolean dfs1(int v, Map<Integer, Integer> color1, Map<Integer, Integer> color2, Deque<T> path) {
        color1.put(v, GRAY);
        for (T symbol : get(v).keySet()) {
            for (int u : get(v).get(symbol)) {
                if (Objects.equals(color1.get(u), WHITE)) {
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
        color1.put(v, BLACK);
        return false;
    }

    private boolean dfs2(int v, Map<Integer, Integer> color1, Map<Integer, Integer> color2, Deque<T> path) {
        color2.put(v, GRAY);
        for (T symbol : get(v).keySet()) {
            for (int u : get(v).get(symbol)) {
                if (Objects.equals(color1.get(u), GRAY)) {
                    path.addLast(symbol);
                    return true;
                }
                if (Objects.equals(color2.get(u), WHITE)) {
                    path.addLast(symbol);
                    if (dfs2(u, color1, color2, path)) {
                        return true;
                    }
                    path.removeLast();
                }
            }
        }
        color2.put(v, BLACK);
        return false;
    }
}