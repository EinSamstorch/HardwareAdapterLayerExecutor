package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
import fake.LatheExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 车床测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheExecutorTest {
    private static SocketConnector sc;

    @BeforeAll
    static void startServer() {
        new StartSocketServer();
        sc = new SocketConnector();
        sc.init();

        new LatheExecutor().start();
        StartSocketServer.sleep(500);
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
        extra.put("processStep", 1);
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void grabItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "grab_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void releaseItemTest() {
        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "release_item");
        message.put("extra", "");

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }
}
