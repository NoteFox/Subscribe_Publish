import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class Runner extends Thread{

    public static Runner instance;
    private boolean isRunning = false;

    private final String SWAP_CONNECTION = "c";
    private final String SWAP_PUBLISHER = "p";

    private final String PUBLISH = "publish";
    private final String NEW_SUB = "new";
    private final String ANSWER = "answer";

    private final String EXIT = "exit";

    private String input;
    private InputStream is = System.in;
    private MODE mode = MODE.NULL;

    private ArrayList<Connector> connectorList = new ArrayList<>();

    public Runner() {
        this.instance = this;

        new ConnectionGetter().start();
        new Publisher().start();
    }

    private synchronized void read(String s) {
        switch (s) {
            case SWAP_CONNECTION:
                mode = MODE.SUBSCRIBER;
                System.out.println("swapped to connect");
                break;
            case SWAP_PUBLISHER:
                mode = MODE.PUBLISHER;
                System.out.println("swapped to publisher");
                break;
            case PUBLISH:
                if (mode != MODE.PUBLISHER) {
                    System.err.println("NOT A PUBLISHER");
                    break;
                }
                Publisher.instance.publish("published content");
                break;

            case NEW_SUB :
                if (mode != MODE.SUBSCRIBER) {
                    System.err.println("NOT A SUBSCRIBER");
                    break;
                }
                connectorList.add(new Connector());
                connectorList.get(connectorList.size() - 1).start();
                break;

            case ANSWER :
                if (mode != MODE.SUBSCRIBER) {
                    System.err.println("NOT A SUBSCRIBER");
                    break;
                }

                try {
                    for (int i = 0; i < connectorList.size(); i++) {
                        connectorList.get(i).answer("Answer");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;

            case EXIT:
                System.err.println("exiting");

                Publisher.instance.interrupt();
                ConnectionGetter.instance.interrupt();

                System.exit(0);

            default:
                System.err.println("not a real command");
        }
    }

    @Override
    public void run() {
        isRunning = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        do {
            try {
                System.out.println("Current mode: " + mode.toString());
                //System.out.println("Input: ");
                input = br.readLine();
                read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (isRunning);
    }
}
