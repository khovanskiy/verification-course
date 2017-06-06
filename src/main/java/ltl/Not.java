package ltl;

import lombok.Data;

@Data
public class Not extends Formula {
    private final Formula f;

    @Override
    public String toString(){
        return "!" + f;
    }

    @Override
    public Formula toNormalForm(boolean negation) {
        return f.toNormalForm(!negation);
    }
}
