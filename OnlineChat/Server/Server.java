import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int PORT = 8888;
    private ServerSocket serverSocket;
    private Socket socket;
    
    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");
            while (true) {
                socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}