package com.group2.kahootclone.controller;

import com.group2.kahootclone.object.Request.slideController.SlideRequest;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.ISlideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/presentation/{presentationId}/slide")
public class SlideController {
    @Autowired
    ISlideService slideService;
    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)" +
            "or @presentationRole.isMember(authentication, #presentationId)")
    @GetMapping("")
    public ResponseEntity<ResponseObject<List<SlideResponse>>> getSlideOfPresentation (@PathVariable int presentationId){
        ResponseObject<List<SlideResponse>> presentationRes = slideService.getSlidesOfPresentation(presentationId);
        return presentationRes.createResponse();
    }

    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)")
    @PostMapping("")
    public ResponseEntity<ResponseObject<SlideResponse>> createSlide (@PathVariable int presentationId, @RequestBody SlideRequest request){
        ResponseObject<SlideResponse> slideRet = slideService.createSlide(presentationId, request);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)")
    @PutMapping("/{slideId}")
    public ResponseEntity<ResponseObject<SlideResponse>> updateSlide (@PathVariable int presentationId,
                                                                      @PathVariable int slideId,
                                                                      @RequestBody SlideRequest request){
        ResponseObject<SlideResponse> slideRet = slideService.updateSlide(slideId, request);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)")
    @GetMapping("/{slideId}")
    public ResponseEntity<ResponseObject<SlideResponse>> getSlide (@PathVariable int presentationId,
                                                                      @PathVariable int slideId){
        ResponseObject<SlideResponse> slideRet = slideService.getSlide(slideId);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isOwner(authentication, #presentationId) " +
            "or @presentationRole.isCoOwner(authentication, #presentationId)")
    @DeleteMapping("/{slideId}")
    public ResponseEntity<ResponseObject<Boolean>> deleteSlide (@PathVariable int presentationId,
                                                                      @PathVariable int slideId){
        ResponseObject<Boolean> slideRet = slideService.deleteSlide(slideId);
        return slideRet.createResponse();
    }
}
