package com.group2.kahootclone.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.eventHandlers.RoomHandler;
import com.group2.kahootclone.socket.eventHandlers.SlideHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class WebsocketBroadcastHandler extends TextWebSocketHandler {
    @Autowired
    RoomHandler roomHandler;
    @Autowired
    SlideHandler slideHandler;
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    Map<String, Map<String, Set<WebSocketSession>>> roomMap = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            log.info(message.getPayload());

            SocketRequest socketRequest = new ObjectMapper().readValue(message.getPayload(), SocketRequest.class);

            switch (socketRequest.getMetaData().getMessageType()){
                case JOIN_ROOM:
                    roomHandler.handleJoinRoom(session, roomMap, socketRequest);
                    break;
                case LEAVE_ROOM:
                    roomHandler.handleLeaveRoom(session, roomMap, socketRequest);
                    break;
                case VOTE_SLIDE:
                    slideHandler.handleVoteSlide(session, roomMap, socketRequest);
                    break;
                case PRESENT_SLIDE:
                    slideHandler.handlePresentSlide(session, roomMap, socketRequest);
                    break;
                default:
                    log.error("Invalid message type {}", socketRequest.getMetaData());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.error("Having connection from {}", session.getUri());
        sessions.add(session);
    }
}
