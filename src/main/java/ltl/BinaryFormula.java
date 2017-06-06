package ltl;

import lombok.Data;

@Data
public class BinaryFormula implements Formula{
    private final BinaryOperation operation;
    private final Formula left;
    private final Formula right;

    @Override
    public String toString(){
        return "(" + left + " " + operation.toString() + " " + right + ")";
    }
}
