package ltl;

import lombok.Data;

@Data
public class Next <T> extends Formula<T> {
    private final Formula<T> f;

    @Override
    public String toString(){
        return "(N " + f + ")";
    }

    @Override
    public Formula<T> toNormalForm(boolean negation) {
        return new Next<>(f.toNormalForm(negation));
    }

    @Override
    public void accept(LTLVisitor<T> visitor) {
        visitor.visit(this);
    }

    @Override
    public Formula<T> negation() {
        return this;
    }
}
