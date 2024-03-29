package com.group2.kahootclone.config;

import com.group2.kahootclone.socket.WebsocketBroadcastHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    @Autowired
    WebsocketBroadcastHandler broadcastHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(broadcastHandler, "/socket")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");
    }
}
