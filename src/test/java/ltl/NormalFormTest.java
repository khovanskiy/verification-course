package ltl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class NormalFormTest {
    @Test
    public void simpleTests(){
        Assert.assertEquals(LTL.parse("'a'").toNormalForm(), LTL.parse("'a'"));
        Assert.assertEquals(LTL.parse("!'a'").toNormalForm(), LTL.parse("!'a'"));
        Formula f = LTL.parse("!((('a' & 'b') U !('a' | 'b')) R !(!'c'))").toNormalForm();
        Formula ref = LTL.parse("(!'a' | !'b' R 'a' | 'b') U (!'c')");
        Assert.assertEquals(ref, f);
    }
}
