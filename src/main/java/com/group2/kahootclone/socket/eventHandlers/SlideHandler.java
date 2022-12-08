package com.group2.kahootclone.socket.eventHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.constant.socket.ClientType;
import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.ISlideService;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.slideHandler.PresentSlideRequest;
import com.group2.kahootclone.socket.Request.slideHandler.RecordRequest;
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
public class SlideHandler {
    @Autowired
    ISlideService slideService;

    public void handlePresentSlide(WebSocketSession session,
                                   Map<String, Map<String, Set<WebSocketSession>>> roomMap,
                                   SocketRequest<PresentSlideRequest> message) {
        try {
            //set
            ResponseObject<SlideResponse> slideResponse = slideService.presentSlide(message.getMessage().getSlideId());

            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.PRESENTED_SLIDE);

            SocketResponse<SlideResponse> socketResponse = SocketResponse
                    .<SlideResponse>builder()
                    .metaData(metaData)
                    .message(slideResponse.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(message.getMetaData().getRoomName());

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

    public void handleVoteSlide(WebSocketSession session,
                                Map<String, Map<String, Set<WebSocketSession>>> roomMap,
                                SocketRequest<RecordRequest> message) {
        try {
            //get
            RecordRequest recordRequest = message.getMessage();

            //set
            ResponseObject<SlideResponse> slideResponse = slideService.saveRecord(recordRequest);
            slideResponse = slideService.getSlide(recordRequest.getSlideId());

            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.UPDATED_SLIDE);

            SocketResponse<SlideResponse> socketResponse = SocketResponse
                    .<SlideResponse>builder()
                    .metaData(metaData)
                    .message(slideResponse.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(message.getMetaData().getRoomName());

            if (subRoomMap != null && subRoomMap.get(ClientType.HOST.toString()) != null)
                for (WebSocketSession socketSession : subRoomMap.get(ClientType.HOST.toString())) {
                    socketSession.sendMessage(new TextMessage(responseStr));
                }

            socketResponse.setMessage(null);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(socketResponse)));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
