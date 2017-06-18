package model.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class Graph {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final Map<Node, Map<Edge, Node>> successors = new HashMap<>();
    private AtomicInteger ids = new AtomicInteger(10000);

    public Map<Node, Map<Edge, Node>> getSuccessors() {
        return successors;
    }

    public Node getById(Integer id) {
        return nodeMap.computeIfAbsent(id, k -> new Node(id));
    }

    public void add(Node a, Edge edge, Node b) {
        successors.computeIfAbsent(a, k -> new HashMap<>()).put(edge, b);
    }

    public Node getNew() {
        Integer id = ids.getAndIncrement();
        return nodeMap.computeIfAbsent(id, k -> new Node(id));
    }

    public int getNodesSize() {
        return successors.size();
    }
}
