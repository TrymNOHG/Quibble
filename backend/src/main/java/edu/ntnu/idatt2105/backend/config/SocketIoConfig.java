package edu.ntnu.idatt2105.backend.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Class for configuring and setting the parameters of the socket.io server.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.3 30.03.2024
 */
@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class SocketIoConfig {
    private final Logger logger = Logger.getLogger(SocketIoConfig.class.getName());
    private final Environment env;
    private SocketIOServer socketIOServer;
    private boolean isTesting = false;

    /**
     * Bean method for creating a socket.io server.
     *
     * @return a socket.io server
     */
    @Bean
    public SocketIOServer socketIOServer() {
        AtomicBoolean isTest = new AtomicBoolean(false);
        if(isTest.get()) {
            isTesting = true;
            return null;
        }
        AtomicReference<String> host = new AtomicReference<>("localhost");
        Configuration config = new Configuration();
        Arrays.stream(env.getActiveProfiles()).forEach(env -> {
            if (env.equals("prod")) {
                logger.info("Running in docker container. Setting correct host.");
                config.setOrigin("*");
                host.set("0.0.0.0");
            }
        });

        config.setHostname(host.get());
        config.setPort(3000);

        socketIOServer = new SocketIOServer(config);
        return socketIOServer;
    }

    /**
     * Method for stopping the socket.io server when the application is shut down.
     */
    @PreDestroy
    public void stopSocketIOServer() {
        if (isTesting)
            return;
        if(socketIOServer == null) {
            return;
        }
        logger.info("Stopping socket.io server");
        socketIOServer.stop();
    }
}
