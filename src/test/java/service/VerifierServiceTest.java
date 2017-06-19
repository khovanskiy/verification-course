package service;

import org.junit.Before;

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
}
