package ltl;

import lombok.Data;

@Data
public class Release implements Formula {
    private final Formula l;
    private final Formula r;
}
