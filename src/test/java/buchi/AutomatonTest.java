package buchi;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

@Slf4j
public class AutomatonTest {
    @Test
    public void loopPathTest(){
        log.info("loop test");
        Automaton<String> a = new Automaton<>(1);
        a.addTransition(0, 0, "a");
        Assert.assertTrue(a.findAWord().isEmpty());

        a.setAccepting(0);
        Assert.assertEquals(asList("a"), new ArrayList<>(a.findAWord()));
    }

    @Test
    public void tailedPathTest(){
        log.info("lasso test");
        int n = 10;
        Automaton<Integer> a = new Automaton<>(n + 1);
        for(int i = 0; i < n - 1; i++){
            a.addTransition(i, i + 1, i);
        }
        a.addTransition(n - 1, n / 2, n - 1);
        a.setAccepting(n / 2 - 1);
        Assert.assertTrue(a.findAWord().isEmpty());

        a.setAccepting(n / 2 + 1);
        List<Integer> ref = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Assert.assertEquals(ref, new ArrayList<>(a.findAWord()));
    }

    @Test
    public void simpleIntersectionTest(){
        log.info("intersection test");
        int an = 2;
        int bn = 4;
        Automaton<Integer> a = new Automaton<>(an);
        Automaton<Integer> b = new Automaton<>(bn);
        a.addTransition(0, 1, 0);
        a.addTransition(1, 0, 1);

        int curr = 0;
        for(int i = 0; i < bn - 1; i++){
            b.addTransition(i, i + 1, curr);
            curr = 1 - curr;
        }
        b.addTransition(3, 0, 1);

        a.setAccepting(0);
        b.setAccepting(1);

        Automaton<Integer> c = Automaton.intersect(a, b);
        List<Integer> path = new ArrayList<>(c.findAWord());
        List<Integer> ref = IntStream.iterate(1, i -> i % 2).limit(path.size()).boxed().collect(Collectors.toList());
        Assert.assertEquals(path, ref);
    }
}
