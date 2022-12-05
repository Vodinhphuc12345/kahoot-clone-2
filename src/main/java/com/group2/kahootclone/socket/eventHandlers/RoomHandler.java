package com.group2.kahootclone.socket.eventHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.roomHanler.RoomRequest;
import com.group2.kahootclone.socket.Response.MetaData;
import com.group2.kahootclone.socket.Response.SocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Slf4j
@Component
public class RoomHandler {
    public void handleJoinRoom(WebSocketSession session,
                               Map<String, Map<String, Set<WebSocketSession>>> roomMap,
                               SocketRequest<RoomRequest> socketRequest) {
        try {
            //get
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.computeIfAbsent(socketRequest.getMetaData().getRoomName(), k -> new HashMap<>());
            Set<WebSocketSession> sessions = subRoomMap.computeIfAbsent(socketRequest.getMetaData().getClientType().toString(), k -> new LinkedHashSet<>());

            //set
            sessions.add(session);

            //reset
            subRoomMap.put(socketRequest.getMetaData().getClientType().toString(), sessions);
            roomMap.put(socketRequest.getMetaData().getRoomName(), subRoomMap);

            //send
            MetaData metaData = MetaData.builder().messageType(ServerMessageType.JOIN_ROOM).build();
            SocketResponse<Object> socketResponse = SocketResponse
                    .builder()
                    .metaData(metaData)
                    .message(null)
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            for (WebSocketSession s : sessions) {
                s.sendMessage(new TextMessage(responseStr));
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }

    }

    public void handleLeaveRoom(WebSocketSession session,
                                Map<String, Map<String, Set<WebSocketSession>>> roomMap,
                                SocketRequest<RoomRequest> socketRequest) {
        try {
            //get
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(socketRequest.getMetaData().getRoomName());
            if (subRoomMap == null) return;

            Set<WebSocketSession> sessions = subRoomMap.get(socketRequest.getMetaData().getClientType().toString());
            if (sessions == null) return;

            //set
            sessions.remove(session);

            //send
            MetaData metaData = MetaData.builder().messageType(ServerMessageType.LEAVE_ROOM).build();
            SocketResponse<Object> socketResponse = SocketResponse
                    .builder()
                    .metaData(metaData)
                    .message(null)
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            for (WebSocketSession s : sessions) {
                s.sendMessage(new TextMessage(responseStr));
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
