package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
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

    @BeforeAll
    static void startServer() {
        new StartSocketServer();
        sc = new SocketConnector();
        sc.init();

        new WarehouseExecutor().start();
        StartSocketServer.sleep(1000);
    }

    @Test
    void importItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "import_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void exportItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "export_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void moveItem() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        int itemPos = 10;
        JSONObject extra = new JSONObject();
        extra.put("from", itemPos);
        extra.put("to", 15);
        message.put("task_no", taskNo);
        message.put("cmd", "move_item");
        message.put("extra", extra);

        sc.sendMessage(message);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
    }
}
