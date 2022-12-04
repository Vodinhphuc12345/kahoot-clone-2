package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.mapper.SlideMapper;
import com.group2.kahootclone.model.Presentation;
import com.group2.kahootclone.model.Slide;
import com.group2.kahootclone.object.Request.slideController.SlideRequest;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.SlideRepository;
import com.group2.kahootclone.service.Interface.ISlideService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SlideService implements ISlideService {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    SlideRepository slideRepository;
    @Override
    public ResponseObject<List<SlideResponse>> getSlidesOfPresentation(int presentationId) {
        ResponseObject<List<SlideResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //get slide of  presentation
            List<SlideResponse> list = presentation.getSlides()
                    .stream()
                    .map(SlideResponse::fromSlide)
                    .collect(Collectors.toList());
            //build success
            ret.setObject(list);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<SlideResponse> createSlide(int presentationId, SlideRequest request) {
        ResponseObject<SlideResponse> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //get slide of  presentation
            Slide slide = request.toSlide();
            slide.setPresentation(presentation);
            Slide savedSlide = slideRepository.save(slide);
            //build success
            ret.setObject(SlideResponse.fromSlide(savedSlide));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<SlideResponse> updateSlide(int slideId, SlideRequest request) {
        ResponseObject<SlideResponse> ret = new ResponseObject<>();
        try {
            //group
            Optional<Slide> slideRet = slideRepository.findById(slideId);
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.buildResourceNotFound("Slide not found.");
                return ret;
            }

            //update of  slide
            Mappers.getMapper(SlideMapper.class).slideRequestToSlide(request, slide);
            Slide savedSlide = slideRepository.save(slide);
            //build success
            ret.setObject(SlideResponse.fromSlide(savedSlide));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<Boolean> deleteSlide(int slideId) {
        ResponseObject<Boolean> ret = new ResponseObject<>();
        try {
            //group
            Optional<Slide> slideRet = slideRepository.findById(slideId);
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.buildResourceNotFound("Slide not found.");
                return ret;
            }
            //delete slide
            slideRepository.delete(slide);
            //build success
            ret.setObject(true);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<SlideResponse> getSlide(int slideId) {
        ResponseObject<SlideResponse> ret = new ResponseObject<>();
        try {
            //group
            Optional<Slide> slideRet = slideRepository.findById(slideId);
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.buildResourceNotFound("Slide not found.");
                return ret;
            }
            //build success
            ret.setObject(SlideResponse.fromSlide(slide));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
