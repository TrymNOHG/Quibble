package edu.ntnu.idatt2105.backend.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
@org.springframework.context.annotation.Configuration
public class SocketIoConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3000);

        return new SocketIOServer(config);
    }
}
