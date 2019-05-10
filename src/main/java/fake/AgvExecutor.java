package fake;

import commons.Commands;

import java.util.Collections;

/**
 * AGV执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvExecutor extends AbstractExecutor {
    private static final String CMD_MOVE = "move";

    public AgvExecutor() {
        cmdList = Collections.singletonList(CMD_MOVE);
    }

    public static void main(String[] args) {
        new AgvExecutor().start();
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        if (CMD_MOVE.equals(cmd)) {
            actionResult = Commands.agvMove(extra);
        }
        return actionResult;
    }
}
