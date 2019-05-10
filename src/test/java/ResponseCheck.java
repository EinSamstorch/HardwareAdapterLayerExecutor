import com.alibaba.fastjson.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


class ResponseCheck {
    static void checkResponseSuccess(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "success", "");
    }

    static void checkResponseSuccess(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "success", extra);
    }

    static void checkResponseFailed(String actionResponse, int taskNo) {
        check(actionResponse, taskNo, "failed", null);
    }

    static void checkResponseFailed(String actionResponse, int taskNo, String extra) {
        check(actionResponse, taskNo, "failed", extra);
    }

    private static void check(String response, int taskNo, String result, String extra) {
        JSONObject jsonActionResponse = JSONObject.parseObject(response);
        assertEquals(jsonActionResponse.getIntValue("task_no"), taskNo);
        assertEquals(jsonActionResponse.getString("result"), result);

        if (null != extra) {
            assertEquals(jsonActionResponse.getString("extra"), extra);
        }
    }
}
