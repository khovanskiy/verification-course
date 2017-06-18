package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Const<T> extends Formula<T> {
    private final boolean isTrue;

    public Const(boolean isTrue) {
        this.isTrue = isTrue;
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if (negation) {
            return new Const<>(!isTrue);
        } else {
            return this;
        }
    }

    @Override
    public void accept(LTLVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public Formula<T> negation() {
        return new Const<>(!isTrue);
    }

    @Override
    public String toString() {
        return isTrue ? "true" : "false";
    }
}
