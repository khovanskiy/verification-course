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
    public void parse() {
        Formula ref = ltlService.parse("('a' | ('b' R 'c'))");
        Formula test = ltlService.parse("'a' | 'b' R 'c'");
        Assert.assertEquals(ref, test);
        ref = ltlService.parse("('a' && (X 'b') U ((F 'a') & 'b'))");
        test = ltlService.parse("'a' && X 'b' U (F 'a' & 'b')");
        Assert.assertEquals(ref, test);
    }
}
