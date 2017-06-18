package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

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
    public <N> Formula<N> map(Map<T, N> mapping, N defaultValue) {
        return new Variable<>(mapping.getOrDefault(name, defaultValue));
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
