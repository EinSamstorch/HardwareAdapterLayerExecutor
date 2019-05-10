package fake;

import commons.Commands;

import java.util.Arrays;

/**
 * 虚拟仓库执行层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseExecutor extends AbstractExecutor {
    private static final String CMD_IMPORT_ITEM = "import_item";
    private static final String CMD_EXPORT_ITEM = "export_item";
    private static final String CMD_MOVE_ITEM = "move_item";

    public WarehouseExecutor() {
        cmdList = Arrays.asList(CMD_MOVE_ITEM,
                CMD_IMPORT_ITEM,
                CMD_EXPORT_ITEM);
    }

    public static void main(String[] args) {
        new WarehouseExecutor().start();
    }

    @Override
    protected boolean actionExecute(int taskNo, String cmd, String extra) {
        boolean actionResult = false;
        switch (cmd) {
            case CMD_IMPORT_ITEM:
                actionResult = Commands.importItem();
                break;
            case CMD_EXPORT_ITEM:
                actionResult = Commands.exportItem();
                break;
            case CMD_MOVE_ITEM:
                actionResult = Commands.warehouseMoveItem(extra);
                break;
        }
        return actionResult;
    }

}
