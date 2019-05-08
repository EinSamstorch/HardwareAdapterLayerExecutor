package fake;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import commons.LoggerUtil;
import commons.SocketListener;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 虚拟执行层模板
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public abstract class AbstractExecutor extends SocketListener {
    private static final String FIELD_TASK_NO = "task_no";
    private static final String FIELD_CMD = "cmd";
    private static final String FIELD_EXTRA = "extra";
    private static final String FIELD_RESULT = "result";
    private static final String FIELD_RESULT_SUCCESS = "success";
    private static final String FIELD_RESULT_FAILED = "failed";

    /**
     * 对接收到的消息进行完整性检查 并处理该消息
     * @param msg
     */
    @Override
    protected void messageHandler(String msg) {
        LoggerUtil.machine.debug(msg);

        JSONObject jsonMsg;
        try {
            jsonMsg = JSONObject.parseObject(msg);
            // 检查完整性
            checkMessage(jsonMsg);
        }catch (JSONException |MissingMessagePartException e) {
            // JSON 解析异常 或者 消息完整性检查异常

            // 将 stackTrace 写入到 logger中
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionMsg = sw.toString();
            LoggerUtil.machine.warn(exceptionMsg);
            return;
        }
        int taskNo = jsonMsg.getIntValue(FIELD_TASK_NO);
        String cmd = jsonMsg.getString(FIELD_CMD);
        String extra = jsonMsg.getString(FIELD_EXTRA);

        this.cmdHandler(taskNo, cmd, extra);
    }

    /**
     * 对命令进行解析处理
     * @param taskNo 任务号
     * @param cmd 命令
     * @param extra 额外参数
     */
    protected abstract void cmdHandler(int taskNo, String cmd, String extra);


    /**
     * 校验消息完整性
     * @param jsonMsg 消息结构体
     * @throws MissingMessagePartException 消息结构缺失异常
     */
    private void checkMessage(JSONObject jsonMsg) throws MissingMessagePartException {
        if (null == jsonMsg) {
            throw new NullPointerException();
        }
        boolean missingMessagePart = null == jsonMsg.get(FIELD_TASK_NO)
                || null == jsonMsg.get(FIELD_CMD)
                || null == jsonMsg.get(FIELD_EXTRA);
        if (missingMessagePart) {
            throw new MissingMessagePartException();
        }
    }

    /**
     * 回复消息解析成功
     * @param taskNo 任务号
     */
    protected void replyCmdParseSuccess(int taskNo) {
        JSONObject reply = new JSONObject();

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_RESULT, FIELD_RESULT_SUCCESS);
        reply.put(FIELD_EXTRA, "");
        sendMessage(reply);
    }

    /**
     * 回复消息解析失败
     * @param taskNo 任务号
     * @param reason 失败原因
     */
    protected void replyCmdParseFailed(int taskNo, String reason) {
        JSONObject reply = new JSONObject();

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_RESULT, FIELD_RESULT_FAILED);
        reply.put(FIELD_EXTRA, reason);
        sendMessage(reply);
    }

    /**
     * 回复动作执行成功
     * @param taskNo 任务号
     */
    protected void replyActionSuccess(int taskNo) {
        JSONObject reply = new JSONObject();

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_RESULT, FIELD_RESULT_FAILED);
        reply.put(FIELD_EXTRA, "");
        sendMessage(reply);
    }

    /**
     * 回复动作执行失败
     * @param taskNo 任务号
     * @param reason 原因
     */
    protected void replyActionFailed(int taskNo, String reason) {
        JSONObject reply = new JSONObject();

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_RESULT, FIELD_RESULT_FAILED);
        reply.put(FIELD_EXTRA, reason);
        sendMessage(reply);
    }

    /**
     * 消息结构缺失异常
     */
    private class MissingMessagePartException extends Exception {}
}
