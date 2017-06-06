package ltl;

public enum BinaryOperation {
    R {
        @Override
        public Formula toNormalForm(Formula left, Formula right) {
            return new BinaryFormula(U, left.toNormalForm(true), right.toNormalForm(true));
        }
    }, U {
        @Override
        public Formula toNormalForm(Formula left, Formula right) {
            return new BinaryFormula(R, left.toNormalForm(true), right.toNormalForm(true));
        }
    }, OR {
        @Override
        public Formula toNormalForm(Formula left, Formula right) {
            return new BinaryFormula(AND, left.toNormalForm(true), right.toNormalForm(true));
        }
    }, AND {
        @Override
        public Formula toNormalForm(Formula left, Formula right) {
            return new BinaryFormula(OR, left.toNormalForm(true), right.toNormalForm(true));
        }
    };

    /**
     * Transforms negation of the corresponding formula
     */
    public abstract Formula toNormalForm(Formula left, Formula right);
}
