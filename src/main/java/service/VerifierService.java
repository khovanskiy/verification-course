package service;

public class VerifierService {
    private final AutomatonService automatonService;
    private final LtlService ltlService;

    public VerifierService(AutomatonService automatonService, LtlService ltlService) {
        this.automatonService = automatonService;
        this.ltlService = ltlService;
    }
}