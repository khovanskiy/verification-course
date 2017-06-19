package model.ltl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BinaryFormula<T> extends Formula<T> {
    private final BinaryOperation operation;
    private final Formula<T> left;
    private final Formula<T> right;

    public BinaryFormula(BinaryOperation operation, Formula<T> left, Formula<T> right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + operation.toString() + " " + right + ")";
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if (negation) {
            return operation.toNormalForm(left, right);
        } else {
            return new BinaryFormula<>(operation, left.toNormalForm(false), right.toNormalForm(false));
        }

    }

    @Override
    public <N> Formula<N> map(Function<? super T, ? extends N> mapper) {
        return new BinaryFormula<>(operation, left.map(mapper), right.map(mapper));
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
