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

public class SocketConnector {
    private Socket socket;

    private void connect() {
        try {
            socket = new Socket("127.0.0.1", 5656);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerType() {
        JSONObject type = new JSONObject();
        type.put("type", "agent");
        send(type.toJSONString());

    }

    public void init() {
        this.connect();
        this.registerType();
    }

    public void sendMessage(Object msg) {
        JSONObject msgStruct = new JSONObject();
        msgStruct.put("to", "machine");
        msgStruct.put("message", msg);
        send(msgStruct.toJSONString());
    }

    public String receiveMessage() {
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            line = br.readLine();
        } catch (IOException e) {
            LoggerUtil.machine.error(e.getMessage(), e);
        }
        return line;
    }

    private void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerUtil.machine.error(e.getMessage());
        }
    }
}
