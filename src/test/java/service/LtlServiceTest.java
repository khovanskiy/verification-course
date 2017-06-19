package service;

import lombok.extern.slf4j.Slf4j;
import model.ltl.Formula;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class LtlServiceTest {
    private LtlService ltlService = new LtlService();

    @Test
    public void simpleTest() {
        log.info("simple test");
        log.info("a & b: " + ltlService.parse("'a' <-> 'b'"));
        Assert.assertEquals(ltlService.parse("'a' & 'b'"), ltlService.parse("('a' & 'b')"));
        Assert.assertNotEquals(ltlService.parse("'b' & 'a'"), ltlService.parse("('a' & 'b')"));
    }

    @Test
    public void priorityTest() {
        log.info("operators' priority test");
        Formula ref = ltlService.parse("('c' -> ('d' -> ('a' & !('b')))) <-> 'e'");
        Formula test = ltlService.parse("'c' -> 'd' -> 'a' & !'b' <-> 'e'");
        Assert.assertEquals(ref, test);

        ref = ltlService.parse("X(G(F(('ab' & 'b') U ('c' | 'd'))))");
        test = ltlService.parse("XGF 'ab' & 'b' U 'c' | 'd'");
        Assert.assertEquals(ref, test);
    }
}
