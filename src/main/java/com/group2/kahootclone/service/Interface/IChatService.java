package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.DTO.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatService {
    ResponseObject<List<ChatResponse>> getChatOfPresentation(int presentationId, int fromChat);

    ResponseObject<ChatResponse> addChat(ChatRequest message);
}
