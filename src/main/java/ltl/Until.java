package ltl;

import lombok.Data;

@Data
public class Until implements Formula {
    private final Formula l;
    private final Formula r;
}
