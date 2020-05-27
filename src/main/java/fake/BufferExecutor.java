package fake;

import commons.Commands;
import java.util.Arrays;

/**
 * 工位台.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class BufferExecutor extends AbstractExecutor {

  private static final String CMD_IMPORT = "import_item";
  private static final String CMD_EXPORT = "export_item";


  public BufferExecutor(int port) {
    super(port);
    cmdList = Arrays.asList(CMD_EXPORT, CMD_IMPORT);
  }

  public static void main(String[] args) {
    int port = 5656;
    if(args.length == 1) {
      port = Integer.parseInt(args[0]);
    } else if(args.length != 0) {
      throw new IllegalArgumentException("Wrong argument");
    }
    new BufferExecutor(port).start();
  }

  @Override
  protected boolean actionExecute(int taskNo, String cmd, String extra) {
    boolean in = CMD_IMPORT.equals(cmd);
    return Commands.imExport(in, extra);

  }
}
