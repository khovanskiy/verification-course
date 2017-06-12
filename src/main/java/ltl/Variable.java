package ltl;

import lombok.Data;

@Data
public class Variable <T> extends Formula<T> {
    private final T name;

    @Override
    public String toString(){
        return name.toString();
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        if(negation){
            return new Not<>(this);
        } else {
            return this;
        }
    }
}
