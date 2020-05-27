package executor;

import com.alibaba.fastjson.JSONObject;
import commons.ResponseCheck;
import commons.SocketConnector;
import commons.StartSocketServer;
import fake.AgvExecutor;
import fake.BufferExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 工位台测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class BufferExecutorTest {
  private static SocketConnector sc;

  @BeforeAll
  static void startServer() {
    new StartSocketServer();
    BufferExecutorTest.sc = new SocketConnector();
    sc.init();

    new BufferExecutor(5656).start();
    StartSocketServer.sleep(500);
  }

  @Test
  void exportItemTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 1;

    JSONObject extra = new JSONObject();
    extra.put("buffer_no", 9);
    msg.put("task_no",taskNo);
    msg.put("cmd", "export_item");
    msg.put("extra", extra);

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
  }

  @Test
  void importItemTest() {
    JSONObject msg = new JSONObject();
    int taskNo = 2;

    JSONObject extra = new JSONObject();
    extra.put("buffer_no", 11);
    msg.put("task_no",taskNo);
    msg.put("cmd", "import_item");
    msg.put("extra", extra);

    sc.sendMessage(msg);
    String actionResponse = sc.receiveMessage();
    ResponseCheck.checkActionResponseSuccess(actionResponse, taskNo);
  }
}
