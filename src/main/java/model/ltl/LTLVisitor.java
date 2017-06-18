package model.ltl;

public interface LTLVisitor<T> {
    void visit(BinaryFormula<T> binary);

    void visit(Const<T> c);

    void visit(Not<T> formula);

    void visit(Next<T> formula);

    void visit(Variable<T> variable);
}
