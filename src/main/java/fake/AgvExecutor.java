package fake;

import com.alibaba.fastjson.JSONObject;
import commons.LoggerUtil;

/**
 * AGV执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvExecutor extends AbstractExecutor {
    private static final String CMD_IMPORT_ITEM = "import_item";
    private static final String CMD_EXPORT_ITEM = "export_item";
    private static final String CMD_MOVE = "move";

    /**
     * 检查命令是否有效， 并执行任务动作
     * @param taskNo 任务号
     * @param cmd 命令
     * @param extra 额外参数
     */
    @Override
    protected void cmdHandler(int taskNo, String cmd, String extra) {
        // 检查是否支持该命令
        switch(cmd){
            case CMD_EXPORT_ITEM:
            case CMD_MOVE:
            case CMD_IMPORT_ITEM:
                break;
            default: replyCmdParseFailed(taskNo, "Cmd Unknown"); return;
        }
        // 1. 回复命令解析成功
        replyCmdParseSuccess(taskNo);
        // 2. 执行该命令
        boolean actionResult = false;
        switch (cmd) {
            case CMD_IMPORT_ITEM:
                actionResult = importItem();
                break;
            case CMD_EXPORT_ITEM:
                actionResult = exportItem();
                break;
            case CMD_MOVE:
                actionResult = move(extra);
                break;
        }
        // 3. 回复命令执行结果
        if(actionResult) {
            replyActionSuccess(taskNo);
        } else {
            replyActionFailed(taskNo, "");
        }
    }

    /**
     * 接收货物
     * @return
     */
    private boolean importItem() {
        try {
            Thread.sleep(500);
            LoggerUtil.machine.info("import item successful");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 送出货物
     * @return
     */
    private boolean exportItem() {
        try {
            Thread.sleep(500);
            LoggerUtil.machine.info("export item successful");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移动
     * @param extra 移动点
     * @return
     */
    private boolean move(String extra) {
        JSONObject jsonExtra = JSONObject.parseObject(extra);
        final String FIELD_LOCATION = "map_location";
        int location = jsonExtra.getIntValue(FIELD_LOCATION);
        try {
            Thread.sleep(50000);
            LoggerUtil.machine.info(String.format("reach point %d successful", location));
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new AgvExecutor().start();
    }
}
