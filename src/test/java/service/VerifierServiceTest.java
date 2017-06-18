package service;

import model.ltl.Formula;
import model.verifier.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static model.ltl.LTL.*;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class VerifierServiceTest {
    private AutomatonService automatonService;

    private VerifierService verifierService;

    @Before
    public void setUp() {
        automatonService = new AutomatonService();
        verifierService = new VerifierService(automatonService);
    }

    @Test
    public void create() {
        Formula<String> f = globally(future(var("p")));
        Set<Node<String>> set = verifierService.create(f);
        set.size();
    }

    @Test
    public void check() {
        //verifierService.check();
    }
}
