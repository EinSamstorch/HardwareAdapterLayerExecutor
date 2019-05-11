package commons;

import com.alibaba.fastjson.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class ResponseCheck {
    private static void check(String response, int taskNo, String resultName, String result, String extra) {
        JSONObject jsonActionResponse = JSONObject.parseObject(response);
        assertEquals(jsonActionResponse.getIntValue("task_no"), taskNo);
        assertEquals(jsonActionResponse.getString(resultName), result);

        if (null != extra) {
            assertEquals(jsonActionResponse.getString("extra"), extra);
        }
    }

    public static void checkActionResponseSuccess(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "action_result", "success", "");
    }

    public static void checkActionResponseSuccess(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "action_result", "success", extra);
    }

    public static void checkActionResponseFailed(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "action_result", "failed", null);
    }

    public static void checkActionResponseFailed(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "action_result", "failed", extra);
    }

    public static void checkCmdResponseSuccess(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "cmd_result", "success", "");
    }

    public static void checkCmdResponseSuccess(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "cmd_result", "success", extra);
    }

    public static void checkCmdResponseFailed(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "cmd_result", "failed", null);
    }

    public static void checkCmdResponseFailed(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "cmd_result", "failed", extra);
    }
}
