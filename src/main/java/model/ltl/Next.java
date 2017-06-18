package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Next<T> extends Formula<T> {
    private final Formula<T> f;

    public Next(Formula<T> f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "(N " + f + ")";
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        return new Next<>(f.toNormalForm(negation));
    }

    @Override
    public <N> Formula<N> map(Map<T, N> mapping, N defaultValue) {
        return new Next<>(f.map(mapping, defaultValue));
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
