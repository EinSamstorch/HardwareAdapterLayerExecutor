package commons;

import com.alibaba.fastjson.JSONObject;

/**
 * 硬件支持的命令.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class Commands {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预估加工时间，此处将工件所有尺寸之和作为预估时间
     *
     * @return 预估加工时间
     */
    public static float evaluate(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);
        JSONObject detailSize = jsonExtra.getJSONObject("detailSize");
        if (null == detailSize) {
            throw new IllegalArgumentException("Detail size not found");
        }
        float totalTime = 0.0f;
        for (Object v : detailSize.values()) {
            totalTime += Float.parseFloat((String) v);
        }

        if (totalTime <= 0.00000001) {
            throw new IllegalArgumentException("Size Error");
        }
        return totalTime;
    }

    /**
     * 通用加工
     *
     * @param extra
     * @return
     */
    public static boolean generalProcess(String extra) {
        sleep(30000);
        LoggerUtil.machine.info("Process Finished.");
        return true;
    }

    /**
     * 接收货物
     *
     * @return
     */
    public static boolean importItem() {
        sleep(500);
        LoggerUtil.machine.info("Import item successful");
        return true;
    }

    /**
     * 送出货物
     *
     * @return
     */
    public static boolean exportItem() {
        sleep(500);
        LoggerUtil.machine.info("Export item successful");
        return true;
    }

    /**
     * AGV移动
     *
     * @param extra 移动点
     * @return
     */
    public static boolean agvMove(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);

        final String FIELD_DESTINATION = "destination";
        final String FIELD_ACTION = "action";
        final String ACTION_IMPORT = "import";
        final String ACTION_EXPORT = "export";
        int location = jsonExtra.getIntValue(FIELD_DESTINATION);
        if (location <= 0) {
            throw new IllegalArgumentException("Map location can not be zero or negative.");
        }

        String action = jsonExtra.getString(FIELD_ACTION);
        if (null == action) {
            throw new IllegalArgumentException("No action found.");
        }
        switch (action) {
            case ACTION_EXPORT:
            case ACTION_IMPORT:
                break;
            default:
                throw new IllegalArgumentException("Invalid action.");
        }
        sleep(30000);
        LoggerUtil.machine.info(String.format("Arrive point: %d, and action: %s", location, action));
        return true;
    }

    public static boolean warehouseMoveItem(String extra) {
        JSONObject jsonExtra = JsonTool.parseObject(extra);

        final String FIELD_FROM = "from";
        final String FIELD_TO = "to";
        int from = jsonExtra.getIntValue(FIELD_FROM);
        int to = jsonExtra.getIntValue(FIELD_TO);
        sleep(15000);
        LoggerUtil.machine.info(String.format("Move item from %d to %d.", from, to));
        return true;
    }
}
