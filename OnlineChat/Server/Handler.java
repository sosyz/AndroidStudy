import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private String message;
    private Socket socket;
    public Handler(Socket socket) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            System.out.println("Handler is running, new accepted socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while ((message = in.readLine()) != null) {
                // utf8编码
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                System.out.println("Client: " + message);
                // 替换字符
                message = message.replace("吗", "");
                message = message.replace("？", "?");
                message = message.replace("?", "!");
                // 回复
                System.out.println("Server: " + message);
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}