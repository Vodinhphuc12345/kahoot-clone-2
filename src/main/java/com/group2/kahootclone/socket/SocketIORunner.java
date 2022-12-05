package com.group2.kahootclone.socket;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketIORunner implements CommandLineRunner {
    private final SocketIOServer server;
    @Override
    public void run(String... args) throws Exception {
//        server.start();
    }
}
