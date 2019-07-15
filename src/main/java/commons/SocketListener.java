package commons;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 连接socket server.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public abstract class SocketListener extends Thread {
    /**
     * 设备类型
     */
    public static final String AGENT = "agent";
    public static final String MACHINE = "machine";
    public static final String RFID = "rfid";

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_TO = "to";
    private static final String FIELD_MESSAGE = "message";
    /**
     * Socket server 监听地址
     */
    private static final String host = "127.0.0.1";
    /**
     * 用于构建 {"type": type_str}, 向 socket server 注册类型
     */
    private JSONObject selfType = new JSONObject();
    /**
     * Socket Server 默认端口 5656
     */
    private int port;
    /**
     * Socket连接实例
     */
    private Socket socket;

    /**
     * 默认监听5656端口
     */
    public SocketListener() {
        this(5656);
    }

    public SocketListener(int port) {
        this.port = port;
        selfType.put(FIELD_TYPE, MACHINE);
    }


    /**
     * 监听Socket Server转发来的消息，并对消息进行完整性检查 解析处理消息
     */
    @Override
    public void run() {
        this.connect();
        while (true) {
            String receiveStr = this.receive();
            messageHandler(receiveStr);
        }
    }

    /**
     * 连接Socket Server
     */
    private void connect() {
        try {
            LoggerUtil.machine.info("Connecting to Socket Server, port: " +port);
            socket = new Socket(host, port);
            if (socket.isConnected()) {
                LoggerUtil.machine.info("Connected Successfully");
                send(selfType.toJSONString());
            }
        } catch (IOException e) {
            LoggerUtil.machine.error(e.getMessage(), e);
        }
    }

    /**
     * 发送字符串
     *
     * @param words 待发送字符串
     */
    private synchronized void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerUtil.machine.error(e.getMessage());
        }
    }

    /**
     * 封装消息并发送至socket server
     *
     * @param to      接收消息对象
     * @param message 消息内容体
     */
    private void sendMessage(String to, Object message) {
        JSONObject msgStruct = new JSONObject();
        msgStruct.put(FIELD_TO, to);
        msgStruct.put(FIELD_MESSAGE, message);
        send(msgStruct.toJSONString());
    }

    /**
     * 默认发送消息给Agent
     *
     * @param message 消息内容体
     */
    protected void sendMessage(Object message) {
        sendMessage(AGENT, message);
    }

    /**
     * 从socket server 接收消息
     *
     * @return 接收到的字符串，失败返回Null
     */
    private synchronized String receive() {
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            line = br.readLine();
        } catch (IOException e) {
            LoggerUtil.machine.error(e.getMessage(), e);
        }
        return line;
    }

    /**
     * 处理接收到的消息字符串
     *
     * @param msg
     */
    protected abstract void messageHandler(String msg);
}
