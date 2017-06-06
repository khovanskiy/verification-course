package ltl;

import lombok.Data;

@Data
public class Not implements Formula {
    private final Formula f;
}
