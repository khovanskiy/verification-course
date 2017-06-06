package ltl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class LTL {
    public static final Formula P = new Variable("p");
    public static final Formula TRUE = new Or(P, new Not(P));
    public static final Formula FALSE = new Not(TRUE);

    static Formula and(Formula l, Formula r){
        return new Not(new Or(new Not(l), new Not(r)));
    }

    static Formula impl(Formula l, Formula r){
        return new Or(new Not(l), r);
    }
    
    static Formula eq(Formula l, Formula r){
        return LTL.and(LTL.impl(l, r), LTL.impl(r, l));
    }

    static Formula future(Formula f){
        return new Until(TRUE, f);
    }

    static Formula globally(Formula f){
        return new Release(FALSE, f);
    }

    public static Formula parse(String str){
        CharStream in = CharStreams.fromString(str);
        LTLLexer lexer = new LTLLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LTLParser parser = new LTLParser(tokens);
        return parser.ltl().f;
    }
}
