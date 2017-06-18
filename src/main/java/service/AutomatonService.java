package service;

import model.buchi.Automaton;
import model.diagram.*;
import model.graph.ActionEdge;
import model.graph.Edge;
import model.graph.EventEdge;
import model.graph.StateEdge;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class AutomatonService {
    private static final String PREFIX = "s";

    public Automaton<Edge> convertToGraph(Diagram diagram) {
        Map<Integer, Widget> widgetMap = diagram.getWidget().stream().collect(Collectors.toMap(Widget::getId, Function.identity()));
        Map<Integer, Integer> successors = new HashMap<>();
        for (Widget widget : diagram.getWidget()) {
            if (widget instanceof StateWidget) {
                StateWidget state = (StateWidget) widget;
                if (state.getAttributes().getIncomings() == null) {
                    continue;
                }
                for (Incoming incoming : state.getAttributes().getIncomings()) {
                    Widget prev = widgetMap.get(incoming.getId());
                    if (prev instanceof TransitionWidget) {
                        successors.put(incoming.getId(), widget.getId());
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        Automaton<Edge> automaton = new Automaton<>(1);
        AtomicInteger ids = new AtomicInteger(10000);
        Integer initId = null;
        for (Widget widget : diagram.getWidget()) {
            if (widget instanceof StateWidget) {
                StateWidget state = (StateWidget) widget;
                Integer stateNode = state.getId();
                if (state.getAttributes().getIncomings() == null) {
                    if (initId == null) {
                        initId = ids.getAndIncrement();
                    }
                    Edge stateEdge = new StateEdge(state);
                    automaton.addTransition(initId, stateNode, stateEdge);
                }
                if (state.getAttributes().getOutgoings() == null) {
                    continue;
                }
                for (Outgoing outgoing : state.getAttributes().getOutgoings()) {
                    Widget next = widgetMap.get(outgoing.getId());
                    if (next instanceof TransitionWidget) {
                        // event
                        Edge eventEdge = new EventEdge(next.getAttributes().getEvent());
                        Integer eventNodeId = ids.getAndIncrement();
                        automaton.addTransition(stateNode, eventNodeId, eventEdge);

                        // action
                        Edge actionEdge = new ActionEdge(next.getAttributes().getActions());
                        Integer actionNodeId = ids.getAndIncrement();
                        automaton.addTransition(eventNodeId, actionNodeId, actionEdge);

                        // next state
                        Integer successorId = successors.get(outgoing.getId());
                        StateWidget nextState = (StateWidget) widgetMap.get(successorId);
                        Integer nextNode = nextState.getId();
                        Edge nextEdge = new StateEdge(nextState);
                        automaton.addTransition(actionNodeId, nextNode, nextEdge);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        return automaton;
    }

    public <T> void saveAsDot(Automaton<T> graph, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("digraph G {");
            writer.println("\trankdir = LR;");
            for (Map.Entry<Integer, Map<T, List<Integer>>> entry : graph.getAutomaton().entrySet()) {
                int nodeId = entry.getKey();
                Map<T, List<Integer>> succesors = entry.getValue();
                writer.println("\t" + PREFIX + nodeId + ";");
                for (Map.Entry<T, List<Integer>> outgoing : succesors.entrySet()) {
                    T symbol = outgoing.getKey();
                    List<Integer> next = outgoing.getValue();
                    for (int nextId : next) {
                        writer.println("\t" + PREFIX + nodeId + " -> " + PREFIX + nextId + "[label=\"" + symbol + "\"]");
                    }
                }
            }
            writer.println("}");
        }
    }

    public <T> Automaton<T> intersect(Automaton<T> a, Automaton<T> b) {
        int n = a.size();
        int m = b.size();
        int size = n * m * 2;
        Automaton<T> c = new Automaton<>(size);
        // Build all transitions
        for (Map.Entry<Integer, Map<T, List<Integer>>> e1 : a.getAutomaton().entrySet()) {
            int i = e1.getKey();
            Map<T, List<Integer>> iState = e1.getValue();
            for (Map.Entry<Integer, Map<T, List<Integer>>> e2 : b.getAutomaton().entrySet()) {
                int j = e2.getKey();
                Map<T, List<Integer>> jState = e2.getValue();
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
            // todo: validate this replacement
            for (int i : a.getAutomaton().keySet()) {
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
