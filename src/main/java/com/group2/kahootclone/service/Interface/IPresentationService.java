package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.DTO.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.DTO.Response.slideController.SlideResponse;
import com.group2.kahootclone.DTO.ResponseObject;

import java.util.List;

public interface IPresentationService {
    ResponseObject<PresentationResponse> createPresentation(int userId, PresentationRequest request);

    ResponseObject<PresentationResponse> updatePresentation(int presentationId, PresentationRequest request);

    ResponseObject<Boolean> deletePresentation(int presentationId);

    ResponseObject<List<PresentationResponse>> getPresentedPresentationsOfGroup(int groupId);

    ResponseObject<List<PresentationResponse>> getPresentingPresentationsOfGroup(int groupId);

    ResponseObject<List<PresentationResponse>> getPresentationsOfUser(int userId);

    ResponseObject<PresentationResponse> getPresentation(int presentationId);

    ResponseObject<List<PresentationResponse>> getCollaborationPresentationsOfUser(int userId);

    ResponseObject<List<UserResponse>> getCollaboratorsOfPresentation(int presentationId);

    ResponseObject<List<SlideResponse>> startPresentation(com.group2.kahootclone.socket.Request.slideHandler.PresentationRequest startPresentationRequest);

    ResponseObject<List<SlideResponse>> endPresentation(int presentationId);

    ResponseObject<List<SlideResponse>> nextSlide(int presentationId);

    ResponseObject<List<SlideResponse>> prevSlide(int presentationId);

    ResponseObject<Object> deletePresentationCollaboration(int presentationId, int collaboratorId);
}
