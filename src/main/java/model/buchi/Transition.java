package model.buchi;

import model.ltl.Formula;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class Transition {
    private final Formula<String> expression;
    private final String stateName;

    public Transition(Formula<String> expression, String stateName) {
        this.expression = expression;
        this.stateName = stateName;
    }
}
