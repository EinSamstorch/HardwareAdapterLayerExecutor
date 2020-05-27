package fake;

import java.util.Arrays;

/**
 * 多AGV.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class MultiAgvExecutor extends AbstractExecutor {

  private static final String CMD_MOVE = "move";
  private static final String CMD_IMPORT = "import";
  private static final String CMD_EXPORT = "export";
  private static final String CMD_GET_POSTION = "get_postion";
  private int pos;

  public MultiAgvExecutor(int port, int pos) {
    super(port);
    this.pos = pos;
    cmdList = Arrays.asList(CMD_EXPORT, CMD_IMPORT, CMD_MOVE, CMD_GET_POSTION);
  }

  @SuppressWarnings("Duplicates")
  public static void main(String[] args) {
    int port = 5656;
    int pos = 2;
    if (args.length == 2) {
      port = Integer.parseInt(args[0]);
      pos = Integer.parseInt(args[1]);
    } else if (args.length != 0) {
      throw new IllegalArgumentException("Wrong argument");
    }
    new MultiAgvExecutor(port, pos).start();
  }

  @Override
  protected boolean actionExecute(int taskNo, String cmd, String extra) {

    if (CMD_GET_POSTION.equals(cmd)) {
      extraForActionResult = pos;
      return true;
    }
    if (CMD_IMPORT.equals(cmd) || CMD_EXPORT.equals(cmd)) {
      return true;
    }
    if (CMD_MOVE.equals(cmd)) {
      move(extra);
    }
    return false;
  }

  private void move(String path){
    String[] split = path.split(",");
    for (String s : split) {
      pos = Integer.parseInt(s);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
