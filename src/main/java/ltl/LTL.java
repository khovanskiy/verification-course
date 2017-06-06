package ltl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static ltl.BinaryOperation.*;

public class LTL {
    public static final Formula P = new Variable("p");
    public static final Formula TRUE = new BinaryFormula(OR, P, new Not(P));
    public static final Formula FALSE = new BinaryFormula(AND, P, new Not(P));

    static Formula impl(Formula l, Formula r){
        return new BinaryFormula(OR, new Not(l), r);
    }
    
    static Formula eq(Formula l, Formula r){
        return new BinaryFormula(AND, LTL.impl(l, r), LTL.impl(r, l));
    }

    static Formula future(Formula f){
        return new BinaryFormula(U, TRUE, f);
    }

    static Formula globally(Formula f){
        return new BinaryFormula(R, FALSE, f);
    }

    public static Formula parse(String str){
        CharStream in = CharStreams.fromString(str);
        LTLLexer lexer = new LTLLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LTLParser parser = new LTLParser(tokens);
        return parser.ltl().f;
    }
}
