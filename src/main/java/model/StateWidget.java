package model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
public class StateWidget extends Widget {
    private StateAttributes attributes;

    public StateWidget() {
        super("State");
    }
}
