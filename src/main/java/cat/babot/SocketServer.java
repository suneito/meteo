package cat.babot;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static cat.babot.Util.getCurrentTimestamp;

public class SocketServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private String lastUpdate; // Para almacenar la última actualización
    private WeatherData weatherData = new WeatherData();

    public SocketServer(InetSocketAddress address) {
        super(address);
        startHealthCheck();
    }

    private void sendWeatherData() {
        lastUpdate = getCurrentTimestamp();
        String weatherDataMsg = weatherData.getData();

        for (WebSocket client : getConnections()) {
            client.send(weatherDataMsg);
            logger.info("Sent weather data to {}: {}", client.getRemoteSocketAddress(), weatherDataMsg);
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info("New connection established from {}", conn.getRemoteSocketAddress());
        sendWeatherData();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info("Connection closed: {} - Reason: {}", conn.getRemoteSocketAddress(), reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
    }

    @Override
    public void onStart() {
        logger.info("WebSocket server started successfully on address: {}", getAddress());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sendWeatherData();
            }
        }, 0, 10000); // 10000 ms = 10 s
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            logger.error("Error on connection {}: {}", conn.getRemoteSocketAddress(), ex.getMessage(), ex);
        } else {
            logger.error("Server error: {}", ex.getMessage(), ex);
        }
    }

    private void startHealthCheck() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkServerHealth, 0, 1, TimeUnit.MINUTES);
    }

    private void checkServerHealth() {
        logger.warn("Server health check in process");

        // 1 Check active connections
        int activeConnections = getConnections().size();
        logger.info("\tActive connections: " + activeConnections);

        //2 Resources used
        long memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        logger.info("\tMemory used: " + (memoryUsed / (1024 * 1024)) + " MB");

        //3 DB connection
        boolean dbStatus = checkDatabaseConnection();
        if (!dbStatus) {
            logger.error("\tDatabase is not reachable!");
        } else {
            logger.info("\tDatabase is operational.");
        }
    }

    private boolean checkDatabaseConnection() {
        //TODO
        //Implement logic to check DB connection
        return true;
    }
}

