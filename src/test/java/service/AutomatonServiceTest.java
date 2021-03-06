package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.buchi.Intersector;
import model.buchi.State;
import model.diagram.Diagram;
import model.ltl.Formula;
import model.ltl.LTL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class AutomatonServiceTest {
    private static final String output = "never {\n" +
            "T0_init:\n" +
            "\tif\n" +
            "\t:: (w) || (!z && !y) -> goto accept_all\n" +
            "\t:: (!z) -> goto T0_S4\n" +
            "\t:: (!z && !x) -> goto accept_S5\n" +
            "\tfi;\n" +
            "T0_S4:\n" +
            "\tif\n" +
            "\t:: (1) -> goto T0_S4\n" +
            "\t:: (!x) -> goto accept_S5\n" +
            "\tfi;\n" +
            "accept_S5:\n" +
            "\tif\n" +
            "\t:: (!x) -> goto accept_S5\n" +
            "\tfi;\n" +
            "accept_all:\n" +
            "    skip\n" +
            "}";

    private DiagramService diagramService;
    private SystemService systemService;
    private AutomatonService automatonService;

    @Before
    public void setUp() {
        diagramService = new DiagramService();
        systemService = new SystemService();
        automatonService = new AutomatonService(systemService);
    }

    @Test
    public void createFromDiagram() throws IOException {
        Diagram diagram = diagramService.parseDiagram(new File("data", "AChart.xstd"));
        Automaton<Formula<String>> automaton = automatonService.createFromDiagram(diagram);
        automaton.size();
    }

    @Test
    public void parse() {
        List<State> states = automatonService.parse(output);
        Assert.assertEquals("States count", 4, states.size());
    }

    @Test
    public void map() throws IOException {
        Diagram diagram = diagramService.parseDiagram(new File("data", "AChart.xstd"));
        Automaton<Formula<String>> automatonA = automatonService.createFromDiagram(diagram);
        Automaton<String> automatonB = automatonService.map(automatonA, Formula::toString);
        Assert.assertEquals("Automaton Size", automatonA.size(), automatonB.size());
        Assert.assertEquals("Automaton Initial State", automatonA.getInitialState(), automatonB.getInitialState());
        Assert.assertEquals("Automaton Accepting Set", automatonA.getAcceptingSet(), automatonB.getAcceptingSet());
    }

    @Test
    public void createStateListFromLtl() {
        List<State> states = automatonService.createStateListFromLtl("([] <> x && y) || z -> w");
        Assert.assertEquals("States count", 4, states.size());
    }

    @Test
    public void createFromLtl() {
        Automaton<Formula<String>> automaton = automatonService.createFromLtl("([] <> x && y) || z -> w");
        Assert.assertEquals("States count", 4, automaton.size());
    }

    @Test
    public void emptyTransitions() {
        List<State> states = automatonService.createStateListFromLtl("!true");
        Assert.assertEquals("States count", 1, states.size());
        Assert.assertEquals("Transition count", 0, states.get(0).getTransitions().size());
    }

    @Test
    public void createFromLtlFormula() {
        Formula<String> ltl = LTL.not(LTL.globally(LTL.future(LTL.var("p"))));
        Automaton<Formula<String>> automaton = automatonService.createFromLtl(ltl);
        Automaton<Formula<String>> reference = new Automaton<>();
        reference.addTransition(0, 0, LTL.t());
        Formula<String> notP = LTL.not(LTL.var("p"));
        reference.addTransition(0, 1, notP);
        reference.addTransition(1, 1, notP);
        reference.setAccepting(1);
        reference.setInitialState(0);
        Assert.assertEquals(reference, automaton);
    }

    @Test
    public void simpleIntersectionTest() {
        log.info("intersection test");
        int an = 2;
        int bn = 4;
        Automaton<Integer> a = new Automaton<>();
        Automaton<Integer> b = new Automaton<>();
        a.addTransition(0, 1, 0);
        a.addTransition(1, 0, 1);

        int curr = 0;
        for (int i = 0; i < bn - 1; i++) {
            b.addTransition(i, i + 1, curr);
            curr = 1 - curr;
        }
        b.addTransition(3, 0, 1);

        a.setAccepting(0);
        b.setAccepting(1);

        Automaton<Integer> c = automatonService.intersect(a, b, new MyIntersector());
        List<Integer> path = new ArrayList<>(c.findAWord());
        List<Integer> ref = IntStream.iterate(1, i -> i % 2).limit(path.size()).boxed().collect(Collectors.toList());
        Assert.assertEquals(path, ref);
    }

    private class MyIntersector implements Intersector<Integer> {

        @Override
        public Integer intersect(Integer a, Integer b) {
            return a.equals(b) ? a : null;
        }
    }
}
