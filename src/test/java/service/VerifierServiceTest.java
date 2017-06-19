package service;

import org.junit.Before;

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
}
