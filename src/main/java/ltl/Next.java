package ltl;

import lombok.Data;

@Data
public class Next implements Formula {
    private final Formula f;

    @Override
    public String toString(){
        return "(N " + f + ")";
    }
}
