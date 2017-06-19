package model.buchi;

import java.util.List;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class State {
    private final String name;
    private final List<Transition> transitions;

    public State(String name, List<Transition> transitions) {
        this.name = name;
        this.transitions = transitions;
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
