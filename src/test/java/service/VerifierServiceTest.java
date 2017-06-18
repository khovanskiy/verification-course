package service;

import org.junit.Before;
import org.junit.Test;

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
    public void check() {
        //verifierService.check();
    }
}
