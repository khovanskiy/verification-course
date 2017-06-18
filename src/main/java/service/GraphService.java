package service;

import model.diagram.*;
import model.graph.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class GraphService {
    private static final String PREFIX = "s";

    public Graph convertToGraph(Diagram diagram) {
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
        Graph graph = new Graph();
        Node init = null;
        for (Widget widget : diagram.getWidget()) {
            if (widget instanceof StateWidget) {
                StateWidget state = (StateWidget) widget;
                Node stateNode = graph.getById(state.getId());
                if (state.getAttributes().getIncomings() == null) {
                    if (init == null) {
                        init = new Node("init");
                    }
                    Edge stateEdge = new StateEdge(state);
                    graph.add(init, stateEdge, stateNode);
                }
                if (state.getAttributes().getOutgoings() == null) {
                    continue;
                }
                for (Outgoing outgoing : state.getAttributes().getOutgoings()) {
                    Widget next = widgetMap.get(outgoing.getId());
                    if (next instanceof TransitionWidget) {
                        // event
                        Edge eventEdge = new EventEdge(next.getAttributes().getEvent());
                        Node eventNode = new Node("");
                        graph.add(stateNode, eventEdge, eventNode);

                        // action
                        Edge actionEdge = new ActionEdge(next.getAttributes().getActions());
                        Node actionNode = new Node("");
                        graph.add(eventNode, actionEdge, actionNode);

                        // next state
                        Integer successorId = successors.get(outgoing.getId());
                        StateWidget nextState = (StateWidget) widgetMap.get(successorId);
                        Node nextNode = graph.getById(nextState.getId());
                        Edge nextEdge = new StateEdge(nextState);
                        graph.add(actionNode, nextEdge, nextNode);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        return graph;
    }

    public void convertToDot(Graph graph, File file) throws IOException {
        AtomicInteger ids = new AtomicInteger(10000);
        Map<Node, Integer> idMap = new HashMap<>();
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("digraph G {");
            writer.println("\trankdir = LR;");
            for (Map.Entry<Node, Map<Edge, Node>> entry : graph.getSuccessors().entrySet()) {
                Node node = entry.getKey();
                Integer nodeId = idMap.computeIfAbsent(node, k -> ids.getAndIncrement());
                Map<Edge, Node> succesors = entry.getValue();
                writer.println("\t" + PREFIX + nodeId + " [label=\"" + node.getLabel() + "\"];");
                for (Map.Entry<Edge, Node> outgoing : succesors.entrySet()) {
                    Edge edge = outgoing.getKey();
                    Node next = outgoing.getValue();
                    Integer nextId = idMap.computeIfAbsent(next, k -> ids.getAndIncrement());
                    writer.println("\t" + PREFIX + nodeId + " -> " + PREFIX + nextId + "[label=\"" + edge + "\"]");
                }
            }
            writer.println("}");
        }
    }
}
