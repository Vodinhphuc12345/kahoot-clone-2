package com.group2.kahootclone.controller;

import com.group2.kahootclone.object.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.object.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.IPresentationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/presentation")
public class PresentationController {
    @Autowired
    IPresentationService presentationService;
    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) or @presentationRole.isCoOwner(authentication, #presentationId)")
    @PutMapping ("/{presentationId}")
    public ResponseEntity<ResponseObject<PresentationResponse>> updatePresentation (@PathVariable int presentationId
            , @RequestBody PresentationRequest request){
        ResponseObject<PresentationResponse> presentationRes = presentationService.updatePresentation(presentationId, request);
        return presentationRes.createResponse();
    }
    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) or @presentationRole.isCoOwner(authentication, #presentationId)")
    @DeleteMapping ("/{presentationId}")
    public  ResponseEntity<ResponseObject<Boolean>> deletePresentation (@PathVariable int presentationId){
        ResponseObject<Boolean> presentationRes = presentationService.deletePresentation(presentationId);
        return presentationRes.createResponse();
    }

    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)" +
            "or @presentationRole.isMember(authentication, #presentationId)")
    @GetMapping ("/{presentationId}")
    public  ResponseEntity<ResponseObject<PresentationResponse>> getPresentation (@PathVariable int presentationId){
        ResponseObject<PresentationResponse> presentationRes = presentationService.getPresentation(presentationId);
        return presentationRes.createResponse();
    }
}
