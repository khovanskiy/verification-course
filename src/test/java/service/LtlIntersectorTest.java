package service;

import model.ltl.Formula;
import model.ltl.LTL;
import model.verifier.LTLIntersector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class LtlIntersectorTest {

    private LTLIntersector<String> ltlIntersector;

    @Before
    public void setUp() {
        this.ltlIntersector = new LTLIntersector<>();
    }

    @Test
    public void testIntersection() {
        final String common = "E(machine_type)";
        Formula<String> a = LTL.var(common);
        Formula<String> b = LTL.and(LTL.not(LTL.var("S(Dot")), LTL.var(common));
        Formula<String> expected = LTL.var(common);
        Formula<String> actual = this.ltlIntersector.intersect(a, b);
        Assert.assertEquals(expected, actual);
    }
}
