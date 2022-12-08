package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Request.slideController.SlideRequest;
import com.group2.kahootclone.socket.Request.slideHandler.RecordRequest;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;

import java.util.List;

public interface ISlideService {
    ResponseObject<List<SlideResponse>> getSlidesOfPresentation(int presentationId);

    ResponseObject<SlideResponse> createSlide(int presentationId, SlideRequest request);

    ResponseObject<SlideResponse> updateSlide(int slideId, SlideRequest request);

    ResponseObject<Boolean> deleteSlide(int slideId);

    ResponseObject<SlideResponse> getSlide(int slideId);

    ResponseObject<SlideResponse> presentSlide(int slideId);

    ResponseObject<SlideResponse> saveRecord(RecordRequest data);

    ResponseObject<SlideResponse> getPresentingSlide(int presentationId);
}
