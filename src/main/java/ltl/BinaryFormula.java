package ltl;

import lombok.Data;

@Data
public class BinaryFormula <T> extends Formula<T>{
    private final BinaryOperation operation;
    private final Formula<T> left;
    private final Formula<T> right;

    @Override
    public String toString(){
        return "(" + left + " " + operation.toString() + " " + right + ")";
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if(negation){
            return operation.toNormalForm(left, right);
        } else {
            return new BinaryFormula<>(operation, left.toNormalForm(false), right.toNormalForm(false));
        }

    }
}
