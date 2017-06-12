package ltl;

import lombok.Data;

@Data
public class Next <T> extends Formula<T> {
    private final Formula f;

    @Override
    public String toString(){
        return "(N " + f + ")";
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        return new Next<>(f.toNormalForm(negation));
    }
}
