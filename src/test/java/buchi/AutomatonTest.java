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
    public void simpleTest(){
        Automaton<String> a = new Automaton<>(1);
        List<String> path = new ArrayList<>();
        a.addTransition(0, 0, "a");
        a.findAWord().forEachRemaining(path::add);
        Assert.assertEquals(0, path.size());
        a.setAccepting(0);
        a.findAWord().forEachRemaining(path::add);
        Assert.assertEquals(asList("a"), path);
    }

    @Test
    public void tailedPathTest(){
        int n = 10;
        Automaton<Integer> a = new Automaton<>(n + 1);
        for(int i = 0; i < n - 1; i++){
            a.addTransition(i, i + 1, i);
        }
        a.addTransition(n - 1, n / 2, n - 1);
        a.setAccepting(n / 2 - 1);
        List<Integer> path = new ArrayList<>();
        a.findAWord().forEachRemaining(path::add);
        Assert.assertEquals(0, path.size());
        a.setAccepting(n / 2 + 1);
        a.findAWord().forEachRemaining(path::add);
        Assert.assertEquals(IntStream.range(0, n).boxed().collect(Collectors.toList()), path);

    }
}
