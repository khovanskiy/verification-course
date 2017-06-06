package ltl;

public abstract class Formula {
    /**
     * Transforms formulae to the negative normal form
     * @param negation if true then we transform negation of the formula
     * @return transformed formula
     */
    public abstract Formula toNormalForm(boolean negation);
    public Formula toNormalForm(){
        return toNormalForm(false);
    }

}
