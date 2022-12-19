package com.group2.kahootclone.controller;

import com.group2.kahootclone.Utils.LinkUtils;
import com.group2.kahootclone.DTO.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.DTO.Response.chatHandler.ChatResponse;
import com.group2.kahootclone.DTO.Response.groupController.InvitationResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.DTO.Response.questionHandler.QuestionResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.service.Interface.IChatService;
import com.group2.kahootclone.service.Interface.IInvitationService;
import com.group2.kahootclone.service.Interface.IPresentationService;
import com.group2.kahootclone.service.Interface.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/presentation")
public class PresentationController {
    @Autowired
    IPresentationService presentationService;
    @Autowired
    IChatService chatService;
    @Autowired
    IQuestionService questionService;
    @Autowired
    IInvitationService invitationService;
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isCollaborator(authentication, #presentationId)")
    @PutMapping ("/{presentationId}")
    public ResponseEntity<ResponseObject<PresentationResponse>> updatePresentation (@PathVariable int presentationId
            , @RequestBody PresentationRequest request){
        ResponseObject<PresentationResponse> presentationRes = presentationService.updatePresentation(presentationId, request);
        return presentationRes.createResponse();
    }
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId)")
    @DeleteMapping ("/{presentationId}")
    public  ResponseEntity<ResponseObject<Boolean>> deletePresentation (@PathVariable int presentationId){
        ResponseObject<Boolean> presentationRes = presentationService.deletePresentation(presentationId);
        return presentationRes.createResponse();
    }

    @GetMapping ("/{presentationId}")
    public  ResponseEntity<ResponseObject<PresentationResponse>> getPresentation (@PathVariable int presentationId){
        ResponseObject<PresentationResponse> presentationRes = presentationService.getPresentation(presentationId);
        return presentationRes.createResponse();
    }

    //create presentation for group
    @PostMapping("")
    public ResponseEntity<ResponseObject<PresentationResponse>> createPresentation (@RequestBody PresentationRequest request){
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<PresentationResponse> presentationRes = presentationService.createPresentation(userId, request);
        return presentationRes.createResponse();
    }

    // delete presentation collaboration
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId)")
    @DeleteMapping("/{presentationId}/collaborator/{collaboratorId}")
    public ResponseEntity<ResponseObject<Object>> deletePresentationCollaboration(@PathVariable int presentationId,
                                                                                             @PathVariable int collaboratorId,
                                                                                             HttpServletRequest httpRequest) {
        ResponseObject<Object> deleteRes = presentationService.deletePresentationCollaboration(presentationId, collaboratorId);
        return deleteRes.createResponse();
    }
    // create presentation collaboration invitation
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId)")
    @PostMapping("/{presentationId}/invitation")
    public ResponseEntity<ResponseObject<InvitationResponse>> createPresentationCollaboratorInvitation(@PathVariable int presentationId,
                                                                                                       HttpServletRequest httpRequest) {
        ResponseObject<InvitationResponse> invitationRet = invitationService.createPresentationCollaboratorInvitation(presentationId);

        String fehost = httpRequest.getHeader("origin");
        invitationRet.getObject().setInvitationLink(LinkUtils.buildPresentationCollaboratorInvitationLink(invitationRet.getObject().getCode(),
                fehost));
        return invitationRet.createResponse();
    }

    // join presentation collaboration invitation
    @GetMapping("/invitation/{code}")
    public ResponseEntity<ResponseObject<PresentationResponse>> joinPresentationByLink(@PathVariable String code) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<PresentationResponse> presentationRes = invitationService.joinPresentationCollaboratorPresentationByLink(code, userId);
        return presentationRes.createResponse();
    }

    // get collaborators
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isCollaborator(authentication, #presentationId)")
    @GetMapping("/{presentationId}/collaborator")
    public ResponseEntity<ResponseObject<List<UserResponse>>> getCollaborator(@PathVariable int presentationId) {

        ResponseObject<List<UserResponse>> listUserRet = presentationService.getCollaboratorsOfPresentation(presentationId);
        return listUserRet.createResponse();
    }

    //get chat
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isCollaborator(authentication, #presentationId)" +
            "or @presentationRole.isParticipant(authentication, #presentationId)")
    @GetMapping("/{presentationId}/chat")
    public ResponseEntity<ResponseObject<List<ChatResponse>>> getChatOfPresentation(@PathVariable int presentationId) {
        ResponseObject<List<ChatResponse>> listChatRet = chatService.getChatOfPresentation(presentationId);
        return listChatRet.createResponse();
    }

    //get question
    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isCollaborator(authentication, #presentationId)" +
            "or @presentationRole.isParticipant(authentication, #presentationId)")
    @GetMapping("/{presentationId}/question")
    public ResponseEntity<ResponseObject<List<QuestionResponse>>> getQuestionOfPresentation(@PathVariable int presentationId) {
        ResponseObject<List<QuestionResponse>> listQuestionRet = questionService.getQuestionOfPresentation(presentationId);
        return listQuestionRet.createResponse();
    }
}
