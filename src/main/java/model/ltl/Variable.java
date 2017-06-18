package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Variable<T> extends Formula<T> {
    private final T name;

    public Variable(T name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if (negation) {
            return new Not<>(this);
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
        return this;
    }
}
