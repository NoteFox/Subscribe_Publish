import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class ConnectionGetter extends Thread {

    public static ConnectionGetter instance;
    private boolean isRunning = false;
    private ServerSocket srv;
    private Socket socket;

    private Gson gson = new Gson();

    InputStream dos;

    public ConnectionGetter() {
        this.instance = this;
        try {
            srv = new ServerSocket(2500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getSrv() {
        return srv;
    }

    @Override
    public void run() {
        isRunning = true;
        do {
            try {
                socket = srv.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Publisher.instance.addSocket(socket);
        } while (isRunning);
    }
}
