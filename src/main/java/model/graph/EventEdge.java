package model.graph;

import model.diagram.Event;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class EventEdge extends Edge {
    private final Event event;

    public EventEdge(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "E(" + event.getName() + ")";
    }
}
