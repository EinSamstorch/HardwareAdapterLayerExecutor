package commons;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * JSON异常处理.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class JsonTool {
    public static JSONObject parseObject(String msg) {
        JSONObject jsonMsg;
        if (null == msg || msg.equals("")) {
            throw new IllegalArgumentException("Empty Message");
        }
        try {
            jsonMsg = JSONObject.parseObject(msg);
        } catch (JSONException e) {
            // 将 stackTrace 写入到 logger中
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionMsg = sw.toString();
            LoggerUtil.machine.warn(exceptionMsg);
            throw new IllegalArgumentException("Not a json.");
        }
        return jsonMsg;
    }
}
