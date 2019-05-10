import com.alibaba.fastjson.JSONObject;
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
        new AgvExecutor().start();
        AgvExecutorTest.sc = new SocketConnector();
        sc.init();
        AgvExecutorTest.sleep(500);

    }

    @Test
    void importTest() {

        JSONObject message = new JSONObject();
        int taskNo = 1;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject extra = new JSONObject();
        extra.put("destination", 15);
        extra.put("action", "import");
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void exportItemTest() {

        JSONObject message = new JSONObject();
        int taskNo = 2;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject extra = new JSONObject();
        extra.put("destination", 18);
        extra.put("action", "export");
        message.put("extra", extra);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(actionResponse, taskNo);
    }

    @Test
    void moveWithOutExtraActionTest() {
        JSONObject message = new JSONObject();
        int taskNo = 4;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject mapLocation = new JSONObject();
        mapLocation.put("destination", 15);
        message.put("extra", mapLocation);

        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseFailed(actionResponse, taskNo, "No action found.");
    }

    @Test
    void moveWrongLocationTest() {
        JSONObject message = new JSONObject();
        int taskNo = 5;
        message.put("task_no", taskNo);
        message.put("cmd", "move");

        JSONObject extra = new JSONObject();
        extra.put("destination", -1);
        extra.put("action", "export");
        message.put("extra", extra);


        sc.sendMessage(message);

        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseSuccess(cmdParseResponse, taskNo);

        String actionResponse = sc.receiveMessage();
        ResponseCheck.checkResponseFailed(actionResponse, taskNo, "Map location can not be zero or negative.");
    }

    @Test
    void missingMessageTest() {
        // test miss cmd
        JSONObject message = new JSONObject();
        int taskNo = 3;
        message.put("task_no", taskNo);

        sc.sendMessage(message);
        String cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseFailed(cmdParseResponse, taskNo, "Missing message part.");


        // test miss task_no
        message = new JSONObject();
        sc.sendMessage(message);
        cmdParseResponse = sc.receiveMessage();
        ResponseCheck.checkResponseFailed(cmdParseResponse, 0, "Missing message part.");

    }


}
