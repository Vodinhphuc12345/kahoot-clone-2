package com.group2.kahootclone.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.group2.kahootclone.object.Request.socket.PresentSlideRequest;
import com.group2.kahootclone.object.Request.socket.RecordRequest;
import com.group2.kahootclone.object.Request.socket.RoomRequest;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.ISlideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {

    @Autowired
    ISlideService slideService;

    public SocketModule(SocketIOServer server) {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());

        server.addEventListener("joinRoom", RoomRequest.class, onJoinRoom());
        server.addEventListener("leaveRoom", RoomRequest.class, onLeaveRoom());
        server.addEventListener("presentSlide", PresentSlideRequest.class, onPresentSlide());
        server.addEventListener("voteSlide", RecordRequest.class, onVote());
    }

    private DataListener<RoomRequest> onJoinRoom() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            senderClient.joinRoom(data.getMetaData().getRoomName());
            senderClient.getNamespace().getRoomOperations(data.getMetaData().getRoomName()).sendEvent("joinedMember",
                    String.format("New member joined: %s", senderClient.getSessionId()));
        };
    }

    private DataListener<RoomRequest> onLeaveRoom() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            senderClient.leaveRoom(data.getMetaData().getRoomName());
            senderClient.getNamespace().getRoomOperations(data.getMetaData().getRoomName()).sendEvent("leavedMember",
                    String.format("Member leaved: %s", senderClient.getSessionId()));
        };
    }

    private DataListener<PresentSlideRequest> onPresentSlide() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            int slideId = data.getSlideId();
            ResponseObject<SlideResponse> slideResponse = slideService.presentSlide(slideId);
            senderClient.getNamespace()
                    .getRoomOperations(data.getMetaData().getRoomName())
                    .sendEvent("updatedSlide", slideResponse.getObject());
        };
    }

    private DataListener<RecordRequest> onVote() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            ResponseObject<SlideResponse> slideResponse = slideService.saveRecord(data);
            senderClient.getNamespace()
                    .getRoomOperations(data.getMetaData().getRoomName())
                    .sendEvent("updatedSlide", slideResponse.getObject());
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

}
