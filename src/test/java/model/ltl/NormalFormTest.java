package model.ltl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import service.LtlService;

@Slf4j
public class NormalFormTest {
    private LtlService ltlService = new LtlService();

    @Test
    public void simpleTests() {
        Assert.assertEquals(ltlService.parse("'a'").toNormalForm(), ltlService.parse("'a'"));
        Assert.assertEquals(ltlService.parse("!'a'").toNormalForm(), ltlService.parse("!'a'"));
        Formula f = ltlService.parse("!((('a' & 'b') U !('a' | 'b')) R !(!'c'))").toNormalForm();
        Formula ref = ltlService.parse("(!'a' | !'b' R 'a' | 'b') U (!'c')");
        Assert.assertEquals(ref, f);
    }
}
