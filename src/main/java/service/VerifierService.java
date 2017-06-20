package service;

import model.buchi.Automaton;
import model.ltl.Formula;
import model.ltl.LTL;
import model.verifier.LTLIntersector;

public class VerifierService {
    private final AutomatonService automatonService;

    public VerifierService(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Verify automaton <code>automaton</code> according to ltl formula <code>ltl</code>
     *
     * @param automaton automaton, transitions must have form of conjunction of some variables or be "true"
     * @param ltl       ltl formula
     * @return counterexample if
     */
    public <T> Iterable<Formula<T>> verify(Automaton<Formula<T>> automaton, Formula<T> ltl) {
        Automaton<Formula<T>> ltlAutomaton = automatonService.createFromLtl(LTL.not(ltl));
        LTLIntersector<T> intersector = new LTLIntersector<>();
        Automaton<Formula<T>> c = automatonService.intersect(automaton, ltlAutomaton, intersector);
        Iterable<Formula<T>> example = c.findAWord();
        return example.iterator().hasNext() ? example : null;
    }

    public <T> String exampleToString(Iterable<Formula<T>> example){
        StringBuilder result = new StringBuilder();
        for(Formula<T> step : example){
            result.append(step).append("\n");
        }
        return result.toString();
    }
}