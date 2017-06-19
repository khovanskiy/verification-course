package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.buchi.State;
import model.diagram.Diagram;
import model.graph.Edge;
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
    public void convertation() throws IOException {
        Diagram diagram = diagramService.parseDiagram(new File("data", "AChart.xstd"));
        Automaton<Edge> automaton = automatonService.convertToGraph(diagram);
        automaton.size();
    }

    @Test
    public void parse() {
        List<State> states = automatonService.parse(output);
        Assert.assertEquals("States count", 4, states.size());
    }

    @Test
    public void process() {
        List<State> states = automatonService.buildFromLtl("([] <> x && y) || z -> w");
        Assert.assertEquals("States count", 4, states.size());
    }

    @Test
    public void simpleIntersectionTest() {
        log.info("intersection test");
        int an = 2;
        int bn = 4;
        Automaton<Integer> a = new Automaton<>(an);
        Automaton<Integer> b = new Automaton<>(bn);
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

        Automaton<Integer> c = automatonService.intersect(a, b);
        List<Integer> path = new ArrayList<>(c.findAWord());
        List<Integer> ref = IntStream.iterate(1, i -> i % 2).limit(path.size()).boxed().collect(Collectors.toList());
        Assert.assertEquals(path, ref);
    }
}
