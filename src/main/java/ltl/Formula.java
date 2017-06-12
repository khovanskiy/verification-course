package ltl;

public abstract class Formula <T> {
    /**
     * Transforms formulae to the negative normal form
     * @param negation if true then we transform negation of the formula
     * @return transformed formula
     */
    public abstract Formula<T> toNormalForm(boolean negation);

    public Formula<T> toNormalForm(){
        return toNormalForm(false);
    }

    public abstract void accept(LTLVisitor<T> visitor);

    public abstract Formula<T> negation();

}
