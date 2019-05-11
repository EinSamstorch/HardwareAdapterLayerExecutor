package fake;

import commons.Commands;

import java.util.Arrays;

/**
 * 车床.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheExecutor extends AbstractExecutor {
    private static final String CMD_GRAB_ITEM = "grab_item";
    private static final String CMD_RELEASE_ITEM = "release_item";
    private static final String CMD_EVALUATE = "evaluate";
    private static final String CMD_PROCESS = "process";

    public LatheExecutor() {
        cmdList = Arrays.asList(CMD_EVALUATE,
                CMD_GRAB_ITEM,
                CMD_PROCESS,
                CMD_RELEASE_ITEM);
    }

    public static void main(String[] args) {
        new LatheExecutor().start();
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        switch (cmd) {
            case CMD_EVALUATE:
                actionResult = true;
                extraForActionResult = String.valueOf(Commands.evaluate(extra));
                break;
            case CMD_PROCESS:
                actionResult = Commands.latheProcess(extra);
                break;
            case CMD_GRAB_ITEM:
                actionResult = Commands.grabItem();
                break;
            case CMD_RELEASE_ITEM:
                actionResult = Commands.releaseItem();
                break;
        }
        return actionResult;
    }
}
