package com.group2.kahootclone.socket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.socket.Request.SocketRequest;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AnswerRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AskRequest;
import com.group2.kahootclone.socket.Request.questionHandler.ToggleVotingQuestionRequest;
import com.group2.kahootclone.socket.Request.slideHandler.PresentationRequest;
import com.group2.kahootclone.socket.Request.slideHandler.RecordRequest;
import com.group2.kahootclone.socket.eventHandlers.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
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
    @Autowired
    PresentationHandler presentationHandler;
    @Autowired
    ChatHandler chatHandler;
    @Autowired
    QuestionHandler questionHandler;
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
                    SocketRequest<RecordRequest> voteRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<RecordRequest>>() {});
                    slideHandler.handleVoteSlide(session, roomMap, voteRequest);
                    break;
                case NEXT_SLIDE:
                    SocketRequest<PresentationRequest> nextSlideRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<PresentationRequest>>() {});
                    presentationHandler.handleNextSlide(session, roomMap, nextSlideRequest);
                    break;
                case PREV_SLIDE:
                    SocketRequest<PresentationRequest> prevSlideRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<PresentationRequest>>() {});
                    presentationHandler.handlePrevSlide(session, roomMap, prevSlideRequest);
                    break;
                case START:
                    SocketRequest<PresentationRequest> startPresentationRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<PresentationRequest>>() {});
                    presentationHandler.handleStartPresentation(session, roomMap, startPresentationRequest);
                    break;
                case END:
                    SocketRequest<PresentationRequest> endPresentationRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<PresentationRequest>>() {});
                    presentationHandler.handleEndPresentation(session, roomMap, endPresentationRequest);
                    break;
                case CHAT:
                    SocketRequest<ChatRequest> chatRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<ChatRequest>>() {});
                    chatHandler.handleChat(session, roomMap, chatRequest);
                    break;
                case ANSWER_QUESTION:
                    SocketRequest<AnswerRequest> answerQuestionRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<AnswerRequest>>() {});
                    questionHandler.handleAnswerQuestion(session, roomMap, answerQuestionRequest);
                    break;
                case ASK_QUESTION:
                    SocketRequest<AskRequest> askQuestionRequest = new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<AskRequest>>() {});
                    questionHandler.handleAskQuestion(session, roomMap, askQuestionRequest);
                    break;
                case TOGGLE_VOTING_QUESTION:
                    SocketRequest<ToggleVotingQuestionRequest> toggleQuestionRequest =
                            new ObjectMapper().readValue(message.getPayload(),
                            new TypeReference<SocketRequest<ToggleVotingQuestionRequest>>() {});
                    questionHandler.handleToggleVotingQuestion(session, roomMap, toggleQuestionRequest);
                    break;
                default:
                    log.error("Invalid message type {}", socketRequest.getMetaData());
            }
        } catch (IOException e) {
            sessions.remove(session);
            for (Map.Entry<String, Map<String, Set<WebSocketSession>>> entry: roomMap.entrySet()){
                for (Map.Entry<String, Set<WebSocketSession>> entry1: entry.getValue().entrySet()){
                    entry1.getValue().remove(session);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.error("Having connection from {}", session.getUri());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        for (Map.Entry<String, Map<String, Set<WebSocketSession>>> entry: roomMap.entrySet()){
            for (Map.Entry<String, Set<WebSocketSession>> entry1: entry.getValue().entrySet()){
                entry1.getValue().remove(session);
            }
        }
    }
}
