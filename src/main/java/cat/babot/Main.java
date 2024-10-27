package cat.babot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Websocket server");
        SocketServer socketServer = new SocketServer(new InetSocketAddress("0.0.0.0", 2000));
        socketServer.start();
    }
}