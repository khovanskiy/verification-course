package model.graph;

import model.diagram.StateWidget;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class StateEdge extends Edge {
    private final StateWidget state;

    public StateEdge(StateWidget state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "S(" + state.getAttributes().getName() + ")";
    }
}
