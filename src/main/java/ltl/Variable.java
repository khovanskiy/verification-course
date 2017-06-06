package ltl;

import lombok.Data;

@Data
public class Variable extends Formula {
    private final String name;

    @Override
    public String toString(){
        return name;
    }

    @Override
    public Formula toNormalForm(boolean negation) {
        if(negation){
            return new Not(this);
        } else {
            return this;
        }
    }
}
