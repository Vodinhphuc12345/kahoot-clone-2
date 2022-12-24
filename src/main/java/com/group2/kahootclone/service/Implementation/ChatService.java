package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.presentation.Chat;
import com.group2.kahootclone.DTO.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.repository.ChatRepository;
import com.group2.kahootclone.repository.PresentationRepository;
import com.group2.kahootclone.repository.UserRepository;
import com.group2.kahootclone.service.Interface.IChatService;
import com.group2.kahootclone.socket.Request.chatHandler.ChatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public ResponseObject<List<ChatResponse>> getChatOfPresentation(int presentationId, int fromChatId) {
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
            Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
            List<Chat> chats;
            if (fromChatId != 0){
                chats = chatRepository.findAllByPresentationIdAndIdLessThan(presentationId, fromChatId, pageRequest);
            } else {
                chats = chatRepository.findAllByPresentationId(presentationId, pageRequest);
            }

            List<ChatResponse> list = chats
                    .stream()
                    .map(ChatResponse::fromChat)
                    .collect(Collectors.toList());
            Collections.reverse(list);
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
