package service;

import model.buchi.Automaton;
import model.ltl.Formula;
import model.ltl.LTL;
import model.verifier.LTLIntersector;

public class VerifierService {
    private final AutomatonService automatonService;

    public VerifierService(AutomatonService automatonService, LtlService ltlService) {
        this.automatonService = automatonService;
    }

    public <T> Iterable<Formula<T>> verify(Automaton<Formula<T>> automaton, Formula<T> ltl){
        Automaton<Formula<T>> ltlAutomaton = automatonService.createFromLtl(LTL.not(ltl));
        LTLIntersector<T> intersector = new LTLIntersector<>();
        Automaton<Formula<T>> c = automatonService.intersect(automaton, ltlAutomaton, intersector);
        Iterable<Formula<T>> example = c.findAWord();
        return example.iterator().hasNext() ? example : null;
    }
}