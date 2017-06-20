package service;

import model.ltl.Formula;
import model.ltl.LTLLexer;
import model.ltl.LTLParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class LtlService {
    public Formula<String> parse(String str) {
        CharStream in = CharStreams.fromString(str);
        LTLLexer lexer = new LTLLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LTLParser parser = new LTLParser(tokens);
        Formula<String> ltl = parser.ltl().f;
        return parser.getNumberOfSyntaxErrors() == 0 ? ltl : null;
    }
}
