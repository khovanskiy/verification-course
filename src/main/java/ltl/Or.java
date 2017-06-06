package ltl;

import lombok.Data;

@Data
public class Or implements Formula {
    private final Formula l;
    private final Formula r;
}
