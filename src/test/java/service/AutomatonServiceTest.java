package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    private AutomatonService automatonService;

    @Before
    public void setUp() {
        automatonService = new AutomatonService();
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
