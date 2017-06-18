package model.ltl;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class Formula<T> {
    public Formula<T> toNormalForm() {
        return toNormalForm(false);
    }

    /**
     * Transforms formulae to the negative normal form
     *
     * @param negation if true then we transform negation of the formula
     * @return transformed formula
     */
    public abstract Formula<T> toNormalForm(boolean negation);

    public abstract void accept(LTLVisitor<T> visitor);

    public abstract Formula<T> negation();

}
