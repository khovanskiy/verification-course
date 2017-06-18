package ltl;

import lombok.Data;

@Data
public class Not <T> extends Formula<T> {
    private final Formula<T> f;

    @Override
    public String toString(){
        return "!" + f;
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        return f.toNormalForm(!negation);
    }

    @Override
    public void accept(LTLVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public Formula<T> negation() {
        return f;
    }
}
