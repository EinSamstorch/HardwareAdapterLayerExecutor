import commons.LoggerUtil;
import org.junit.Test;

/**
 * 测试logger.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoggerUtilTest {
    @Test
    public void loggerTest() {
        LoggerUtil.machine.info("This is test");
    }
}
