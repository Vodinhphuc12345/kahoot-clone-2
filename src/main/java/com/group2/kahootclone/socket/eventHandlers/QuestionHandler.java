package com.group2.kahootclone.socket.eventHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.constant.socket.ClientType;
import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import com.group2.kahootclone.object.Response.questionHandler.QuestionResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.IQuestionService;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AnswerRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AskRequest;
import com.group2.kahootclone.socket.Request.questionHandler.ToggleVotingQuestionRequest;
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
public class QuestionHandler {
    @Autowired
    IQuestionService questionService;
    public void handleAnswerQuestion(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<AnswerRequest> answerQuestionRequest) {
        try {
            ResponseObject<QuestionResponse> questionRes = questionService.answerQuestion(answerQuestionRequest.getMessage());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.ANSWERED_QUESTION);

            SocketResponse<QuestionResponse> socketResponse = SocketResponse
                    .<QuestionResponse>builder()
                    .metaData(metaData)
                    .message(questionRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(answerQuestionRequest.getMetaData().getRoomName());

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

    public void handleAskQuestion(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<AskRequest> askQuestionRequest) {
        try {
            ResponseObject<QuestionResponse> questionRes = questionService.askQuestion(askQuestionRequest.getMessage());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.ASKED_QUESTION);

            SocketResponse<QuestionResponse> socketResponse = SocketResponse
                    .<QuestionResponse>builder()
                    .metaData(metaData)
                    .message(questionRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(askQuestionRequest.getMetaData().getRoomName());

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

    public void handleToggleVotingQuestion(WebSocketSession session, Map<String, Map<String, Set<WebSocketSession>>> roomMap, SocketRequest<ToggleVotingQuestionRequest> toggleQuestionRequest) {
        try {
            ResponseObject<QuestionResponse> questionRes = questionService.
                    toggleVotingQuestiontoggleQuestion(toggleQuestionRequest.getMessage().getQuestionId(),
                            toggleQuestionRequest.getMessage().getUserId());
            //send
            MetaData metaData = new MetaData();
            metaData.setMessageType(ServerMessageType.VOTED_QUESTION);

            SocketResponse<QuestionResponse> socketResponse = SocketResponse
                    .<QuestionResponse>builder()
                    .metaData(metaData)
                    .message(questionRes.getObject())
                    .build();

            String responseStr = new ObjectMapper().writeValueAsString(socketResponse);
            Map<String, Set<WebSocketSession>> subRoomMap = roomMap.get(toggleQuestionRequest.getMetaData().getRoomName());

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
