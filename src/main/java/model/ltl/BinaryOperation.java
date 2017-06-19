package model.ltl;

public enum BinaryOperation {
    R {
        @Override
        public <T> Formula<T> toNormalForm(Formula<T> left, Formula<T> right) {
            return new BinaryFormula<>(U, left.toNormalForm(true), right.toNormalForm(true));
        }
    }, U {
        @Override
        public <T> Formula<T> toNormalForm(Formula<T> left, Formula<T> right) {
            return new BinaryFormula<>(R, left.toNormalForm(true), right.toNormalForm(true));
        }
    }, OR {
        @Override
        public <T> Formula<T> toNormalForm(Formula<T> left, Formula<T> right) {
            return new BinaryFormula<>(AND, left.toNormalForm(true), right.toNormalForm(true));
        }

        public String toString() {
            return "||";
        }
    }, AND {
        @Override
        public <T> Formula<T> toNormalForm(Formula<T> left, Formula<T> right) {
            return new BinaryFormula<>(OR, left.toNormalForm(true), right.toNormalForm(true));
        }

        public String toString() {
            return "&&";
        }
    };

    /**
     * Transforms negation of the corresponding formula
     */
    public abstract <T> Formula<T> toNormalForm(Formula<T> left, Formula<T> right);
}
