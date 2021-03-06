package model.diagram;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
public class TransitionWidget extends Widget {
    private TransitionAttributes attributes;

    public TransitionWidget() {
        super("Transition");
    }
}
