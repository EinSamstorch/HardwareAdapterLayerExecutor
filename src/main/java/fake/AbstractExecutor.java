package fake;

import com.alibaba.fastjson.JSONObject;
import commons.JsonTool;
import commons.LoggerUtil;
import commons.SocketListener;

import java.util.List;

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
    private static final String FIELD_CMD_RESULT = "cmd_result";
    private static final String FIELD_ACTION_RESULT = "action_result";
    private static final String FIELD_RESULT_SUCCESS = "success";
    private static final String FIELD_RESULT_FAILED = "failed";

    Object extraForCmdResult;
    Object extraForActionResult;

    List<String> cmdList = null;

    public AbstractExecutor(int port) {
        super(port);
    }

    /**
     * 对接收到的消息进行完整性检查 并处理该消息
     *
     * @param msg
     */
    @Override
    protected void messageHandler(String msg) {
        LoggerUtil.machine.debug(msg);

        JSONObject jsonMsg = JsonTool.parseObject(msg);
        try {
            checkMessage(jsonMsg);
        } catch (IllegalArgumentException | MissingMessagePartException e) {
            // JSON 解析异常 或者 消息完整性检查异常
            LoggerUtil.machine.warn(e.getMessage());
            extraForCmdResult = e.getMessage();
            replyCmdParseFailed(jsonMsg.getIntValue(FIELD_TASK_NO));
            return;
        }
        int taskNo = jsonMsg.getIntValue(FIELD_TASK_NO);
        String cmd = jsonMsg.getString(FIELD_CMD);
        String extra = jsonMsg.getString(FIELD_EXTRA);

        this.cmdHandler(taskNo, cmd, extra);
    }

    /**
     * 对命令进行解析处理
     *
     * @param taskNo 任务号
     * @param cmd    命令
     * @param extra  额外参数
     */
    protected void cmdHandler(int taskNo, String cmd, String extra) {
        // 检查是否支持该命令
        if (!cmdList.contains(cmd)) {
            extraForCmdResult = "Cmd Unknown.";
            replyCmdParseFailed(taskNo);
            return;
        }
        // 1. 回复命令解析成功
        replyCmdParseSuccess(taskNo);
        // 2. 执行该命令
        boolean actionResult = false;
        try {
            actionResult = actionExecute(taskNo, cmd, extra);
        } catch (IllegalArgumentException e) {
            extraForActionResult = e.getMessage();
        }
        // 3. 回复命令执行结果
        if (actionResult) {
            replyActionSuccess(taskNo);
        } else {
            replyActionFailed(taskNo);
        }
    }

    /**
     * 对具体命令进行动作执行
     *
     * @param taskNo 任务号
     * @param cmd    命令
     * @param extra  额外参数
     * @return 任务执行结果
     */
    protected abstract boolean actionExecute(int taskNo, String cmd, String extra);

    /**
     * 校验消息完整性
     *
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
            throw new MissingMessagePartException("Missing message part.");
        }
    }

    /**
     * 回复消息解析成功
     *
     * @param taskNo 任务号
     */
    protected void replyCmdParseSuccess(int taskNo) {
        JSONObject reply = new JSONObject();

        Object extra = extraForCmdResult;
        extraForCmdResult = null;
        if (null == extra) {
            extra = "";
        }

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_CMD_RESULT, FIELD_RESULT_SUCCESS);
        reply.put(FIELD_EXTRA, extra);
        sendMessage(reply);
    }

    /**
     * 回复消息解析失败
     *
     * @param taskNo 任务号
     */
    protected void replyCmdParseFailed(int taskNo) {
        JSONObject reply = new JSONObject();

        Object extra = extraForCmdResult;
        extraForCmdResult = null;
        if (null == extra) {
            extra = "";
        }

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_CMD_RESULT, FIELD_RESULT_FAILED);
        reply.put(FIELD_EXTRA, extra);
        sendMessage(reply);
    }

    /**
     * 回复动作执行成功
     *
     * @param taskNo 任务号
     */
    protected void replyActionSuccess(int taskNo) {
        JSONObject reply = new JSONObject();

        Object extra = extraForActionResult;
        extraForActionResult = null;
        if (null == extra) {
            extra = "";
        }

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_ACTION_RESULT, FIELD_RESULT_SUCCESS);
        reply.put(FIELD_EXTRA, extra);
        sendMessage(reply);
    }

    /**
     * 回复动作执行失败
     *
     * @param taskNo 任务号
     */
    protected void replyActionFailed(int taskNo) {
        JSONObject reply = new JSONObject();

        Object extra = extraForActionResult;
        extraForActionResult = null;
        if (null == extra) {
            extra = "";
        }

        reply.put(FIELD_TASK_NO, taskNo);
        reply.put(FIELD_ACTION_RESULT, FIELD_RESULT_FAILED);
        reply.put(FIELD_EXTRA, extra);
        sendMessage(reply);
    }

    /**
     * 消息结构缺失异常
     */
    private class MissingMessagePartException extends RuntimeException {
        public MissingMessagePartException(String message) {
            super(message);
        }
    }
}
