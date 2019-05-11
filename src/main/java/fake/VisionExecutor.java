package fake;

import commons.Commands;

import java.util.Collections;
import java.util.Objects;


/**
 * 视觉检测仪.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionExecutor extends AbstractExecutor {
    private static final String CMD_CHECK = "check";

    public VisionExecutor() {
        cmdList = Collections.singletonList(CMD_CHECK);
    }

    public static void main(String[] args) {
        new VisionExecutor().start();
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        if (Objects.equals(cmd, CMD_CHECK)) {
            actionResult = Commands.check(extra);
        }
        return actionResult;
    }
}
