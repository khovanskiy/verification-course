package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.diagram.Diagram;
import model.ltl.Formula;
import model.ltl.LTL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class VerifierServiceTest {
    private SystemService systemService;
    private AutomatonService automatonService;
    private LtlService ltlService;
    private DiagramService diagramService;
    private VerifierService verifierService;

    @Before
    public void setUp() {
        ltlService = new LtlService();
        systemService = new SystemService();
        automatonService = new AutomatonService(systemService);
        diagramService = new DiagramService();
        verifierService = new VerifierService(automatonService);
    }

    @Test
    public void verify(){
        Formula<String> alwaysP = ltlService.parse("G'p'");
        Formula<String> nextP = ltlService.parse("X'p'");
        Formula<String> futureA = ltlService.parse("F'a'");
        Formula<String> fail = ltlService.parse("XXXXXXXXG'd'");
        Formula<String> p = LTL.var("p");
        Automaton<Formula<String>> a = new Automaton<>();
        a.addTransition(0, 1, p);
        a.addTransition(1, 2, p);
        a.addTransition(2, 1, LTL.var("a"));
        a.addTransition(2, 3, p);
        a.addTransition(3, 3, LTL.t());
        a.setInitialState(0);
        IntStream.range(0, 4).forEach(a::setAccepting);
        Assert.assertNotNull(verifierService.verify(a, alwaysP));
        Assert.assertNull(verifierService.verify(a, nextP));
        Assert.assertNotNull(verifierService.verify(a, futureA));
        Assert.assertNotNull(verifierService.verify(a, fail));
    }

    @Test
    public void verifyExample() throws IOException {
        Formula<String> ltl = LTL.future(LTL.var("S(Dot)"));
        Diagram diagram = diagramService.parseDiagram(new File("data/VarParser.xstd"));
        Automaton<Formula<String>> automaton = automatonService.createFromDiagram(diagram);
        Iterable<Formula<String>> iterable = verifierService.verify(automaton, ltl);
        if (iterable != null) {
            for (Formula<String> f : iterable) {
                log.info(f.toString());
            }
        }
    }
}
