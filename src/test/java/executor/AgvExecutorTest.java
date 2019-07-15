package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
import fake.AgvExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * 测试虚拟AGV硬件执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

class AgvExecutorTest {
    private static SocketConnector sc;

    @BeforeAll
    static void startServer() {
        new StartSocketServer();
        AgvExecutorTest.sc = new SocketConnector();
        sc.init();

        new AgvExecutor().start();
        StartSocketServer.sleep(500);
    }

    @Test
    void moveTest(){
        JSONObject message = new JSONObject();
        int taskNo = 6;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject extra = new JSONObject();
        extra.put("from", 1);
        extra.put("to", 10);
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkCmdResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void moveWrongLocationTest() {
        JSONObject message = new JSONObject();
        int taskNo = 5;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject extra = new JSONObject();
        extra.put("from", -1);
        extra.put("to", -1);
        message.put("extra", extra);


        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkCmdResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkActionResponseFailed(actionResponse, taskNo, "Map location can not be zero or negative.");
    }

    @Test
    void missingMessageTest() {
        // test miss cmd
        JSONObject message = new JSONObject();
        int taskNo = 3;
        message.put("task_no", taskNo);

        sc.sendMessage(message);
        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkCmdResponseFailed(cmdParseResponse, taskNo, "Missing message part.");


        // test miss task_no
        message = new JSONObject();
        sc.sendMessage(message);
        cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkCmdResponseFailed(cmdParseResponse, 0, "Missing message part.");

    }
}
