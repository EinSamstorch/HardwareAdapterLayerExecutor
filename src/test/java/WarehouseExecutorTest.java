import com.alibaba.fastjson.JSONObject;
import fake.WarehouseExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 仓库测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseExecutorTest {
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
        new WarehouseExecutor().start();
        sc = new SocketConnector();
        sc.init();
        AgvExecutorTest.sleep(1000);
    }

    @Test
    void importItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "import_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void exportItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "export_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void moveItemWithOutExtraArgueTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "move_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseFailed(actionResponse, taskNo);
    }
}
