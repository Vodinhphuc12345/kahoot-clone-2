package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;

import java.util.List;

public interface IChatService {
    ResponseObject<List<ChatResponse>> getChatOfPresentation(int presentationId);

    ResponseObject<ChatResponse> addChat(ChatRequest message);
}
