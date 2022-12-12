package com.group2.kahootclone.socket.eventHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.constant.socket.ClientType;
import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import com.group2.kahootclone.object.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.IChatService;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;
import com.group2.kahootclone.socket.Response.MetaData;
import com.group2.kahootclone.socket.Response.SocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ChatHandler {
    @Autowired
    IChatService chatService;
    public void handleChat(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<ChatRequest> chatRequest) {
        try {
            ResponseObject<ChatResponse> chatRes = chatService.addChat(chatRequest.getMessage());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.CHAT);

            SocketResponse<ChatResponse> socketResponse = SocketResponse
                    .<ChatResponse>builder()
                    .metaData(metaData)
                    .message(chatRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(chatRequest.getMetaData().getRoomName());

            if (subRoomMap != null) {
                if (subRoomMap.get(ClientType.MEMBER.toString()) != null)
                    for (WebSocketSession socketSession : subRoomMap.get(ClientType.MEMBER.toString())) {
                        socketSession.sendMessage(new TextMessage(responseStr));
                    }
                if (subRoomMap.get(ClientType.HOST.toString()) != null)
                    for (WebSocketSession socketSession : subRoomMap.get(ClientType.HOST.toString())) {
                        socketSession.sendMessage(new TextMessage(responseStr));
                    }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
