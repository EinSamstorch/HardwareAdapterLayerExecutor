package fake;

import commons.Commands;

import java.util.Collections;
import java.util.Objects;

/**
 * AGV执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvExecutor extends AbstractExecutor {
    private static final String CMD_MOVE = "move";

    public AgvExecutor(int port) {
        super(port);
        cmdList = Collections.singletonList(CMD_MOVE);
    }

    public AgvExecutor() {
        this(5656);
    }

    public static void main(String[] args) {
        if(args.length == 1) {
            int port = new Integer(args[0]);
            new AgvExecutor(port).start();
        } else if(args.length == 0) {
            new AgvExecutor().start();
        } else {
            throw  new IllegalArgumentException("Wrong argument");
        }
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        if (Objects.equals(cmd, CMD_MOVE)) {
            actionResult = Commands.agvMove(extra);
        }
        return actionResult;
    }
}
