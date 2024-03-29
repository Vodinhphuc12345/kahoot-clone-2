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

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isSharingCreator(authentication, #presentationId)")
    @GetMapping("")
    public ResponseEntity<ResponseObject<List<SlideResponse>>> getSlideOfPresentation(@PathVariable int presentationId) {
        ResponseObject<List<SlideResponse>> presentationRes = slideService.getSlidesOfPresentation(presentationId);
        return presentationRes.createResponse();
    }

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isSharingCreator(authentication, #presentationId)")
    @PostMapping("")
    public ResponseEntity<ResponseObject<SlideResponse>> createSlide(@PathVariable int presentationId, @RequestBody SlideRequest request) {
        ResponseObject<SlideResponse> slideRet = slideService.createSlide(presentationId, request);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isSharingCreator(authentication, #presentationId)")
    @PutMapping("/{slideId}")
    public ResponseEntity<ResponseObject<SlideResponse>> updateSlide(@PathVariable int presentationId,
                                                                     @PathVariable int slideId,
                                                                     @RequestBody SlideRequest request) {
        ResponseObject<SlideResponse> slideRet = slideService.updateSlide(slideId, request);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isSharingCreator(authentication, #presentationId)")
    @GetMapping("/{slideId}")
    public ResponseEntity<ResponseObject<SlideResponse>> getSlide(@PathVariable int presentationId,
                                                                  @PathVariable int slideId) {
        ResponseObject<SlideResponse> slideRet = slideService.getSlide(slideId);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isSharingCreator(authentication, #presentationId)")
    @DeleteMapping("/{slideId}")
    public ResponseEntity<ResponseObject<Boolean>> deleteSlide(@PathVariable int presentationId,
                                                               @PathVariable int slideId) {
        ResponseObject<Boolean> slideRet = slideService.deleteSlide(slideId);
        return slideRet.createResponse();
    }

    @PreAuthorize("@presentationRole.isCreator(authentication, #presentationId) " +
            "or @presentationRole.isParticipant(authentication, #presentationId) ")
    @GetMapping("/presenting")
    public ResponseEntity<ResponseObject<SlideResponse>> getPresentingSlide(@PathVariable int presentationId) {
        ResponseObject<SlideResponse> slideRet = slideService.getPresentingSlide(presentationId);
        return slideRet.createResponse();
    }

    // this is for GA02
    @GetMapping("/presenting/ga02")
    public ResponseEntity<ResponseObject<SlideResponse>> getPresentSlideForGA02(@PathVariable int presentationId) {
        ResponseObject<SlideResponse> slideRet = slideService.getPresentingSlideGA02(presentationId);
        return slideRet.createResponse();
    }
}
