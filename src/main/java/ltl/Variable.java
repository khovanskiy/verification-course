package ltl;

import lombok.Data;

@Data
public class Variable implements Formula {
    private final String name;

    @Override
    public String toString(){
        return name;
    }
}
