import java.io.*;
import java.net.Socket;

public class Connector extends Thread {

    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private DataInputStream dis;
    private DataOutputStream dos;

    void answer(String answer) {
        if (os == null)
            throw new NullPointerException("not yet initialized");

        try {
            dos.writeUTF(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 2500);
            System.err.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);

        for (;;) {
            try {
                System.out.println("waiting for a new message");
                System.out.println(dis.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
