import socket.server.GetSocket;

/**
 * 启动SocketServer.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class StartSocketServer {
    private static Boolean boot = false;

    public StartSocketServer() {
        if (!boot) {
            new GetSocket().start();
            AgvExecutorTest.sleep(1000);
            boot = true;
        }
    }
}
