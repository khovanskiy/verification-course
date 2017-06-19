package service;

import model.buchi.Automaton;
import model.ltl.Formula;
import model.ltl.LTL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class VerifierServiceTest {
    private SystemService systemService;
    private AutomatonService automatonService;
    private LtlService ltlService;
    private VerifierService verifierService;

    @Before
    public void setUp() {
        ltlService = new LtlService();
        systemService = new SystemService();
        automatonService = new AutomatonService(systemService);
        verifierService = new VerifierService(automatonService, ltlService);
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
}
