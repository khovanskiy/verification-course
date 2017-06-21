package model.verifier;

import model.buchi.Intersector;
import model.ltl.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static model.ltl.BinaryOperation.AND;
import static model.ltl.BinaryOperation.OR;

/**
 * This class intersects two formulas, where first is conjunction of variables and second - propositional formula
 *
 * @param <T>
 */
public class LTLIntersector<T> implements Intersector<Formula<T>> {
    @Override
    public Formula<T> intersect(Formula<T> a, Formula<T> b) {
        Set<Variable<T>> variables = Collections.emptySet();
        if (!a.equals(LTL.t())) {
            VariableVisitor visitor = new VariableVisitor();
            a.accept(visitor);
            variables = visitor.getResult();
        }
        MarkVisitor markVisitor = new MarkVisitor(variables);
        b.accept(markVisitor);
        return markVisitor.getMark() ? a : null;
    }

    private class MarkVisitor implements LTLVisitor<T> {
        private boolean mark;
        private Set<Variable<T>> trues;

        public MarkVisitor(Set<Variable<T>> trueVariables) {
            trues = trueVariables;
        }

        public boolean getMark() {
            return mark;
        }

        @Override
        public void visit(BinaryFormula<T> binary) {
            binary.getLeft().accept(this);
            boolean left = mark;
            binary.getRight().accept(this);
            boolean right = mark;
            if (binary.getOperation() == OR) {
                mark = left || right;
                return;
            }
            if (binary.getOperation() == AND) {
                mark = left && right;
                return;
            }
            throw new IllegalArgumentException();
        }

        @Override
        public void visit(Const<T> c) {
            mark = c.isTrue();
        }

        @Override
        public void visit(Not<T> formula) {
            formula.getF().accept(this);
            mark = !mark;
        }

        @Override
        public void visit(Next<T> formula) {
            throw new IllegalArgumentException();
        }

        @Override
        public void visit(Variable<T> variable) {
            mark = trues.contains(variable);
        }
    }

    private class VariableVisitor implements LTLVisitor<T> {
        private Set<Variable<T>> variables = new HashSet<>();

        Set<Variable<T>> getResult() {
            return variables;
        }

        @Override
        public void visit(BinaryFormula<T> binary) {
            if (binary.getOperation() != AND) {
                throw new IllegalArgumentException();
            }
            binary.getLeft().accept(this);
            binary.getRight().accept(this);
        }

        @Override
        public void visit(Const<T> c) {
            throw new IllegalArgumentException();
        }

        @Override
        public void visit(Not<T> formula) {
            throw new IllegalArgumentException();
        }

        @Override
        public void visit(Next<T> formula) {
            throw new IllegalArgumentException();
        }

        @Override
        public void visit(Variable<T> variable) {
            variables.add(variable);
        }


    }
}
