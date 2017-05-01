import app.http.HTTPServer;
import sentiment.SentimentAnalysis;
import webservers.HeartbeatHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DriverSentimentAnalysis {
    public static final String tempdirectory = System.getProperty("java.io.tmpdir");
    // let the ClassLoader be only one -- global variable.
    public static ClassLoader parentLoader;
    public static ClassLoader loader;
    private static int webServerPort = 3050;
    public static final String HEARTBEAT_HANDLER = "/api/heartbeat";

    public static void main(String[] args) throws IOException {
        exec(new Scanner(System.in));
        initServer();
    }

    public static void exec(Scanner sc) throws IOException {
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                parentLoader = ClassLoader.getSystemClassLoader();
                URL tmpURL = null;
                try {
                    tmpURL = new File(tempdirectory).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                loader = new URLClassLoader(new URL[] { tmpURL }, parentLoader);

                while (true) {
                    System.out.print("> ");
                    while(sc.hasNextLine()) {
                        String input = sc.nextLine();
                        input = input.trim();
                        if (input.length() == 0) {
                            continue;
                        }
                        if (input.equals("quit")) {
                            break;
                        }
                        else {
                            float sentimentScore = SentimentAnalysis.getSentimentScore(input);
                            if (sentimentScore < 2) {
                                System.out.println("> Are you in a negative mood?" +
                                        "\nTry http://www.japanese-online.com " +
                                        "\nfor more useful information.\n\n");
                            }
                            continue;
                        }
                    }
                }
            }
        },0, TimeUnit.SECONDS);
    }

    private static void initServer() {
        HTTPServer server = new HTTPServer(webServerPort);
        Map<String,Class> handlersHashMap = new HashMap<>();
        handlersHashMap.put(HEARTBEAT_HANDLER, HeartbeatHandler.class);
        // add the Handlers Map to server
        server.setHandlersHashMap(handlersHashMap);
        server.start();
    }
}
