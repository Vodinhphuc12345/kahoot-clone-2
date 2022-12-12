package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.presentation.Chat;
import com.group2.kahootclone.object.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.ChatRepository;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import com.group2.kahootclone.service.Interface.IChatService;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService implements IChatService {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Transactional
    @Override
    public ResponseObject<List<ChatResponse>> getChatOfPresentation(int presentationId) {
        ResponseObject<List<ChatResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //get chats
            List<ChatResponse> list = presentation.getChats()
                    .stream()
                    .map(ChatResponse::fromChat)
                    .collect(Collectors.toList());
            //build success
            ret.setObject(list);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<ChatResponse> addChat(ChatRequest message) {
        ResponseObject<ChatResponse> ret = new ResponseObject<>();
        try {
            //presentation
            Presentation presentation = presentationRepository.findById(message.getPresentationId()).orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //user
            User user = userRepository.findById(message.getUserId()).orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }

            //set
            Chat chat = message.toChat();
            chat.setUser(user);
            chat.setPresentation(presentation);

            //save
            Chat savedChat = chatRepository.save(chat);
            //build success
            ret.setObject(ChatResponse.fromChat(savedChat));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
