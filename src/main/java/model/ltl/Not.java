package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Not<T> extends Formula<T> {
    private final Formula<T> f;

    public Not(Formula<T> f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "!" + f;
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        return f.toNormalForm(!negation);
    }

    @Override
    public <N> Formula<N> map(Map<T, N> mapping, N defaultValue) {
        return new Not<>(f.map(mapping, defaultValue));
    }

    @Override
    public void accept(LTLVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public Formula<T> negation() {
        return f;
    }
}
