import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class BaseTest {
    @Test
    public void main() {
        log.info("Base Test");
        Assert.assertTrue("Dummy test", 1 == 1);
    }
}
