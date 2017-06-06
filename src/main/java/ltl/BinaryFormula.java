package ltl;

import lombok.Data;

@Data
public class BinaryFormula extends Formula{
    private final BinaryOperation operation;
    private final Formula left;
    private final Formula right;

    @Override
    public String toString(){
        return "(" + left + " " + operation.toString() + " " + right + ")";
    }

    @Override
    public Formula toNormalForm(boolean negation) {
        if(negation){
            return operation.toNormalForm(left, right);
        } else {
            return new BinaryFormula(operation, left.toNormalForm(false), right.toNormalForm(false));
        }

    }
}
