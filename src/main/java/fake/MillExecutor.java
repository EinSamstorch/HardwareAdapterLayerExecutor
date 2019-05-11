package fake;

import commons.Commands;

import java.util.Arrays;

/**
 * 虚拟铣床硬件执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillExecutor extends AbstractExecutor {
    private static final String CMD_EVALUATE = "evaluate";
    private static final String CMD_PROCESS = "process";

    public MillExecutor() {
        cmdList = Arrays.asList(CMD_EVALUATE, CMD_PROCESS);
    }

    public static void main(String[] args) {
        new MillExecutor().start();
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        switch (cmd) {
            case CMD_EVALUATE:
                extraForActionResult = String.valueOf(Commands.evaluate(extra));
                actionResult = true;
                break;
            case CMD_PROCESS:
                actionResult = Commands.generalProcess(extra);
                break;
        }
        return actionResult;
    }
}
