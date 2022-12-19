package com.group2.kahootclone.socket.eventHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.constant.socket.ClientType;
import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import com.group2.kahootclone.DTO.Response.slideController.SlideResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.service.Interface.IKahootGroupService;
import com.group2.kahootclone.service.Interface.IPresentationService;
import com.group2.kahootclone.service.Interface.ISlideService;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.slideHandler.PresentationRequest;
import com.group2.kahootclone.socket.Response.MetaData;
import com.group2.kahootclone.socket.Response.SocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class PresentationHandler {
    @Autowired
    ISlideService slideService;
    @Autowired
    IPresentationService presentationService;
    @Autowired
    IKahootGroupService kahootGroupService;

    public void handleNextSlide(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<PresentationRequest> nextSlideRequest) {
        try {
            ResponseObject<List<SlideResponse>> listSlideRes = presentationService.nextSlide(nextSlideRequest.getMessage().getPresentationId());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.PRESENTED_SLIDE);

            SocketResponse<List<SlideResponse>> socketResponse = SocketResponse
                    .<List<SlideResponse>>builder()
                    .metaData(metaData)
                    .message(listSlideRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(nextSlideRequest.getMetaData().getRoomName());

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

    public void handlePrevSlide(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<PresentationRequest> prevSlideRequest) {
        try {
            ResponseObject<List<SlideResponse>> listSlideRes = presentationService.prevSlide(prevSlideRequest.getMessage().getPresentationId());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.PRESENTED_SLIDE);

            SocketResponse<List<SlideResponse>> socketResponse = SocketResponse
                    .<List<SlideResponse>>builder()
                    .metaData(metaData)
                    .message(listSlideRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(prevSlideRequest.getMetaData().getRoomName());

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

    public void handleStartPresentation(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<PresentationRequest> startPresentationRequest) {
        try {
            ResponseObject<List<SlideResponse>> listSlideRes = presentationService.startPresentation(startPresentationRequest.getMessage());
            //send to presentation
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.PRESENTED_SLIDE);

            SocketResponse<List<SlideResponse>> socketResponse = SocketResponse
                    .<List<SlideResponse>>builder()
                    .metaData(metaData)
                    .message(listSlideRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(startPresentationRequest.getMetaData().getRoomName());

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

            // send to groups
            ResponseObject<List<KahootGroupResponse>> groupRes = kahootGroupService.getKahootGroups(startPresentationRequest.getMessage().getGroupIds());

            MetaData metaDataForGroup = new MetaData();
            metaDataForGroup.setMessageType(ServerMessageType.PRESENTING_PRESENTATION_IN_GROUP);
            SocketResponse<Integer> socketResponseForGroup = SocketResponse
                    .<Integer>builder()
                    .metaData(metaDataForGroup)
                    .message(startPresentationRequest.getMessage().getPresentationId())
                    .build();
            String responseStrForGroup = new ObjectMapper().writeValueAsString(socketResponseForGroup);
            if (groupRes.getObject() != null) {
                for (KahootGroupResponse kahootGroupResponse : groupRes.getObject()) {
                    Map<String, Set<WebSocketSession>> subRoomMapOfGroup = roomMap.get(kahootGroupResponse.getRoomName());
                    if (subRoomMapOfGroup != null) {
                        if (subRoomMapOfGroup.get(ClientType.MEMBER.toString()) != null)
                            for (WebSocketSession socketSession : subRoomMapOfGroup.get(ClientType.MEMBER.toString())) {
                                socketSession.sendMessage(new TextMessage(responseStrForGroup));
                            }
                        if (subRoomMapOfGroup.get(ClientType.HOST.toString()) != null)
                            for (WebSocketSession socketSession : subRoomMapOfGroup.get(ClientType.HOST.toString())) {
                                socketSession.sendMessage(new TextMessage(responseStrForGroup));
                            }
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public void handleEndPresentation(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<PresentationRequest> endPresentationRequest) {
        try {
            ResponseObject<List<KahootGroupResponse>> groupRes = kahootGroupService.getPresentingGroupsOfPresentation
                    (endPresentationRequest.getMessage().getPresentationId());

            ResponseObject<List<SlideResponse>> listSlideRes = presentationService.endPresentation(endPresentationRequest.getMessage().getPresentationId());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.PRESENTED_SLIDE);

            SocketResponse<List<SlideResponse>> socketResponse = SocketResponse
                    .<List<SlideResponse>>builder()
                    .metaData(metaData)
                    .message(listSlideRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(endPresentationRequest.getMetaData().getRoomName());

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


            // send to groups
            MetaData metaDataForGroup = new MetaData();
            metaDataForGroup.setMessageType(ServerMessageType.PRESENTING_PRESENTATION_IN_GROUP);
            SocketResponse<PresentationResponse> socketResponseForGroup = SocketResponse
                    .<PresentationResponse>builder()
                    .metaData(metaDataForGroup)
                    .message(null)
                    .build();
            String responseStrForGroup = new ObjectMapper().writeValueAsString(socketResponseForGroup);
            if (groupRes.getObject() != null) {
                for (KahootGroupResponse kahootGroupResponse : groupRes.getObject()) {
                    Map<String, Set<WebSocketSession>> subRoomMapOfGroup = roomMap.get(kahootGroupResponse.getRoomName());
                    if (subRoomMapOfGroup != null) {
                        if (subRoomMapOfGroup.get(ClientType.MEMBER.toString()) != null)
                            for (WebSocketSession socketSession : subRoomMapOfGroup.get(ClientType.MEMBER.toString())) {
                                socketSession.sendMessage(new TextMessage(responseStrForGroup));
                            }
                        if (subRoomMapOfGroup.get(ClientType.HOST.toString()) != null)
                            for (WebSocketSession socketSession : subRoomMapOfGroup.get(ClientType.HOST.toString())) {
                                socketSession.sendMessage(new TextMessage(responseStrForGroup));
                            }
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
