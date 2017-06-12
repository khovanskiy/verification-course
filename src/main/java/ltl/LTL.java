package ltl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static ltl.BinaryOperation.*;

public class LTL {
    public static <T> Formula<T> t(){
        Const<T> c = new Const<>();
        return new BinaryFormula<>(OR, c, new Not<>(c));
    }

    public static <T> Formula<T> f(){
        return new Not<>(t());
    }

    public static <T> Formula<T> impl(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(OR, new Not<>(l), r);
    }

    public static <T> Formula<T> and(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(AND, l, r);
    }

    public static <T> Formula<T> or(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(OR, l, r);
    }

    public static <T> Formula<T> release(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(R, l, r);
    }

    public static <T> Formula<T> until(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(U, l, r);
    }
    
    public static <T> Formula<T> eq(Formula<T> l, Formula<T> r){
        return new BinaryFormula<>(AND, LTL.impl(l, r), LTL.impl(r, l));
    }

    public static <T> Formula<T> not(Formula<T> f){
        return new Not<>(f);
    }

    public static <T> Formula<T> next(Formula<T> f){
        return new Next<>(f);
    }

    public static <T> Formula<T> var(T var){
        return new Variable<>(var);
    }

    public static <T> Formula<T> future(Formula<T> f){
        return new BinaryFormula<>(U, t(), f);
    }

    public static <T> Formula<T> globally(Formula<T> f){
        return new BinaryFormula<>(R, f(), f);
    }

    public static Formula<String> parse(String str) {
        CharStream in = CharStreams.fromString(str);
        LTLLexer lexer = new LTLLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LTLParser parser = new LTLParser(tokens);
        return parser.ltl().f;
    }
}
