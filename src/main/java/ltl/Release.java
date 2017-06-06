package ltl;

import lombok.Data;

@Data
public class Release implements Formula {
    private final Formula l;
    private final Formula r;

    @Override
    public String toString(){
        return "(" + l + " R " + r + ")";
    }
}
