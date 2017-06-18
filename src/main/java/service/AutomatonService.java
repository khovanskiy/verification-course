package service;

import model.buchi.Automaton;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class AutomatonService {

    public <T> Automaton<T> intersect(Automaton<T> a, Automaton<T> b) {
        int n = a.size();
        int m = b.size();
        int size = n * m * 2;
        Automaton<T> c = new Automaton<>(size);
        // Build all transitions
        for (int i = 0; i < n; i++) {
            Map<T, List<Integer>> iState = a.getAutomaton().get(i);
            for (int j = 0; j < m; j++) {
                Map<T, List<Integer>> jState = b.getAutomaton().get(j);
                Set<T> commonEdges = new LinkedHashSet<>(iState.keySet());
                commonEdges.retainAll(jState.keySet());
                for (T t : commonEdges) {
                    for (int is : iState.get(t)) {
                        for (int js : jState.get(t)) {
                            c.addTransition(convert(i, j, 0, m), convert(is, js, a.isAccepting(i) ? 1 : 0, m), t);
                            c.addTransition(convert(i, j, 1, m), convert(is, js, a.isAccepting(j) ? 0 : 1, m), t);
                        }
                    }
                }
            }
        }
        // Accepting set of the new automaton
        for (int accB : b.getAcceptingSet()) {
            for (int i = 0; i < n; i++) {
                c.setAccepting(convert(i, accB, 1, m));
            }
        }
        c.setInitialState(convert(a.getInitialState(), b.getInitialState(), 0, m));
        return c;
    }

    private static int convert(int stateA, int stateB, int flag, int sizeB) {
        return 2 * stateA * sizeB + 2 * stateB + flag;
    }
}
