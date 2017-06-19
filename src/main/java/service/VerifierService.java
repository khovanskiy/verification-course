package service;

import model.ltl.Formula;

import java.util.HashMap;
import java.util.Map;

public class VerifierService {
    private final AutomatonService automatonService;
    private final LtlService ltlService;

    public VerifierService(AutomatonService automatonService, LtlService ltlService) {
        this.automatonService = automatonService;
        this.ltlService = ltlService;
    }

    public <T> void verify(Formula<T> ltl){
        Map<T, String> variableMap = new HashMap<>();
        ltl = ltl.toNormalForm(true);
        Formula<String> spin = ltl.map(k -> variableMap.computeIfAbsent(k, v -> "v" + variableMap.size()));
        System.err.println(ltlService.toSpin(spin));
    }
}