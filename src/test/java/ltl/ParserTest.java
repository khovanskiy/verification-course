package ltl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class ParserTest {
    @Test
    public void simpleTest(){
        log.info("simple test");
        log.info("a & b: " + LTL.parse("'a' & 'b'"));
        Assert.assertEquals(LTL.parse("'a' & 'b'"), LTL.parse("('a' & 'b')"));
        Assert.assertNotEquals(LTL.parse("'b' & 'a'"), LTL.parse("('a' & 'b')"));
    }
}
