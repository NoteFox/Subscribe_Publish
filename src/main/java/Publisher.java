
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Publisher extends Thread {

    public static Publisher instance = new Publisher();

    private ArrayList<Socket> subscriberList = new ArrayList<>();
    private Vector<InputStream> subListenerList = new Vector<>();
    private DataInputStream subListener;

    public Publisher() {
        this.instance = this;
    }

    void addSocket(Socket socket) {
        try {
            subscriberList.add(socket);
            subListenerList.add(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SequenceInputStream listener = new SequenceInputStream(subListenerList.elements());
        subListener = new DataInputStream(listener);
        this.interrupt();
    }

    private void send(Socket os, String content) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(os.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dos.writeUTF(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void publish(String content) {
        for (int i = 0; i < subscriberList.size(); i++) {
            send(subscriberList.get(i), content);
        }
    }

    public void printSubList() {
        for (int i = 0; i < subscriberList.size(); i++) {
            System.out.println(" NR : " + (i+1) + " ");
        }
    }

    void pause() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    @Override
    public void run() {
        for (;;) {
            try {
                System.out.println("Listener: " + subListener.readUTF());
            } catch (NullPointerException e) {
                try {
                    pause();
                } catch (InterruptedException ex) {
                    System.err.println("subListener : start");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
