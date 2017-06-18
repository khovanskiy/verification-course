package model.graph;

import model.diagram.Action;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class ActionEdge extends Edge {
    private final List<Action> actions;

    public ActionEdge(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        if (this.actions == null || this.actions.isEmpty()) {
            return "true";
        }
        List<String> strings = this.actions.stream()
                .map(a -> "A(" + a.getName() + ")")
                .collect(Collectors.toList());
        return String.join(" & ", strings);
    }
}
