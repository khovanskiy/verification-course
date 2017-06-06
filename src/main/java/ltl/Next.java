package ltl;

import lombok.Data;

@Data
public class Next extends Formula {
    private final Formula f;

    @Override
    public String toString(){
        return "(N " + f + ")";
    }

    @Override
    public Formula toNormalForm(boolean negation) {
        return new Next(f.toNormalForm(negation));
    }
}
