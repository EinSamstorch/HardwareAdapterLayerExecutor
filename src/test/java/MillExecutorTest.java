import com.alibaba.fastjson.JSONObject;
import fake.MillExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Mill.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillExecutorTest {
    private static SocketConnector sc;

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void startServer() {
        new StartSocketServer();
        new MillExecutor().start();
        sc = new SocketConnector();
        sc.init();
        MillExecutorTest.sleep(1000);
    }

    @Test
    void evaluateTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "evaluate");

        JSONObject extra = new JSONObject();
        JSONObject detailSize = new JSONObject();

        detailSize.put("L1", String.valueOf(15.0));
        detailSize.put("L2", String.valueOf(20.0));
        extra.put("detailSize", detailSize);
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo, String.valueOf(35.0));
    }

    @Test
    void processTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "process");

        JSONObject extra = new JSONObject();
        JSONObject detailSize = new JSONObject();

        detailSize.put("L1", String.valueOf(15.0));
        detailSize.put("L2", String.valueOf(20.0));
        extra.put("detailSize", detailSize);
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }
}
