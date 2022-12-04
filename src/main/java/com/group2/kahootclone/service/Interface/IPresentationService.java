package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.object.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.object.ResponseObject;

import java.util.List;

public interface IPresentationService {
    ResponseObject<PresentationResponse> createPresentation(int userId,int groupId, PresentationRequest request);

    ResponseObject<PresentationResponse> updatePresentation(int presentationId, PresentationRequest request);

    ResponseObject<Boolean> deletePresentation(int presentationId);

    ResponseObject<List<PresentationResponse>> getPresentationsOfGroup(int groupId);

    ResponseObject<List<PresentationResponse>> getPresentationsOfUser(int userId);

    ResponseObject<PresentationResponse> getPresentation(int presentationId);
}
