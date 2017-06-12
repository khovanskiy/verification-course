import lombok.extern.slf4j.Slf4j;
import ltl.Formula;
import ltl.LTL;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class ParserTest {
    @Test
    public void simpleTest(){
        log.info("simple test");
        log.info("a & b: " + LTL.parse("'a' <-> 'b'"));
        Assert.assertEquals(LTL.parse("'a' & 'b'"), LTL.parse("('a' & 'b')"));
        Assert.assertNotEquals(LTL.parse("'b' & 'a'"), LTL.parse("('a' & 'b')"));
    }

    @Test
    public void priorityTest(){
        log.info("operators' priority test");
        Formula ref = LTL.parse("('c' -> ('d' -> ('a' & !('b')))) <-> 'e'");
        Formula test = LTL.parse("'c' -> 'd' -> 'a' & !'b' <-> 'e'");
        Assert.assertEquals(ref, test);

        ref = LTL.parse("X(G(F(('a' & 'b') U ('c' | 'd'))))");
        test = LTL.parse("XGF 'a' & 'b' U 'c' | 'd'");
        Assert.assertEquals(ref, test);
    }
}
