package cn.sonui.study;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements Runnable {
    private static final String server = "192.168.3.7";
    private static final int port = 8888;
    private final StringBuilder sb = new StringBuilder();
    private TextView chatMsg;
    //定义一个handler对象,用来刷新界面
    @SuppressLint("HandlerLeak")
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                chatMsg.setText(sb.toString());
            }
        }
    };
    private EditText sendMsg;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatMsg = findViewById(R.id.message);
        sendMsg = findViewById(R.id.sendMsg);
        Button sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            if (sendMsg.getText().toString().length() > 0 && socket != null && socket.isConnected() && !socket.isOutputShutdown()) {
                new Thread(() -> {
                    try {
                        out.println(sendMsg.getText().toString());
                        sb.append("Client: ").append(sendMsg.getText().toString()).append("\n");
                        handler.sendEmptyMessage(0x123);
                        sendMsg.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(server, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w("MainActivity", "run");
        Log.w("MainActivity", "in is " + (in == null ? "null" : "not null"));
        try {
            if (in != null) {
                String msg;
                while (true) {
                    Log.w("gggg", String.valueOf(123));
                    msg = in.readLine();
                    if (msg != null) {
                        sb.append("Server: ").append(msg).append("\n");
                        handler.sendEmptyMessage(0x123);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}