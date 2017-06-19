package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;

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
    public <N> Formula<N> map(Function<? super T, ? extends N> mapper) {
        return new Not<>(f.map(mapper));
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
