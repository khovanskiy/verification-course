package ltl;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Const <T> extends Formula<T> {
    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if(negation){
            return new Not<>(this);
        } else {
            return this;
        }
    }

    @Override
    public String toString(){
        return "p";
    }
}
