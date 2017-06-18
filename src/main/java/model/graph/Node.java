package model.graph;

import lombok.ToString;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@ToString
public class Node {
    private final Integer id;
    private final String label;

    public Node(Integer id) {
        this.id = id;
        this.label = id + "";
    }

    public Node(String label) {
        this.id = -1;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
