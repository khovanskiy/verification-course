package service;

import com.google.common.collect.HashBiMap;
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
        Map<String, T> oldNames = HashBiMap.create(variableMap).inverse();
    }
}