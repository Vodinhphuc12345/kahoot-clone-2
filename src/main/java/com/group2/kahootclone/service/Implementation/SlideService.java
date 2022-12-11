package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.constant.ErrorCodes;
import com.group2.kahootclone.mapper.SlideMapper;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.model.presentation.record.RecordId;
import com.group2.kahootclone.model.presentation.Slide;
import com.group2.kahootclone.object.Request.slideController.SlideRequest;
import com.group2.kahootclone.socket.Request.slideHandler.RecordRequest;
import com.group2.kahootclone.object.Response.slideController.SlideResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.RecordRepository;
import com.group2.kahootclone.reposibility.SlideRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import com.group2.kahootclone.service.Interface.ISlideService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SlideService implements ISlideService {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SlideRepository slideRepository;
    @Autowired
    RecordRepository recordRepository;

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

            if (presentation.getSlides().isEmpty()) {
                slide.setPresenting(true);
            }
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

    @Transactional
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

    @Override
    public ResponseObject<SlideResponse> presentSlide(int slideId) {
        ResponseObject<SlideResponse> ret = new ResponseObject<>();
        try {
            //slide
            Optional<Slide> slideRet = slideRepository.findById(slideId);
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.buildResourceNotFound("Slide not found.");
                return ret;
            }
            //unPresent other slides
            Presentation presentation = slide.getPresentation();
            presentation.getSlides().forEach(s -> {
                s.setPresenting(false);
            });
            presentationRepository.save(presentation);
            //present
            slide.setPresenting(true);
            slideRepository.save(slide);
            //build success
            ret.setObject(SlideResponse.fromSlide(slide));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<SlideResponse> saveRecord(RecordRequest request) {
        ResponseObject<SlideResponse> ret = new ResponseObject<>();
        try {
            //check slide
            Optional<Slide> slideRet = slideRepository.findById(request.getSlideId());
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.buildResourceNotFound("Slide not found.");
                return ret;
            }
            //check user
            Optional<User> userRet = userRepository.findById(request.getUserId());
            User user = userRet.orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }
            //check existed record
            RecordId recordId = new RecordId(user.getId(), slide.getId());
            Record record = recordRepository.findByRecordId(recordId);

            if (record != null) {
                ret.setMessage("You already answered this question.");
                ret.setErrorCode(ErrorCodes.EXISTED);
                return ret;
            }
            //save record
            Record requestRecord = new Record();
            requestRecord.setUser(user);
            requestRecord.setSlide(slide);
            requestRecord.setAnswer(request.getAnswer());

            Record savedRecord = recordRepository.save(requestRecord);
            //build success

            ret.setObject(SlideResponse.fromSlide(savedRecord.getSlide()));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<SlideResponse> getPresentingSlide(int presentationId) {
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
            Optional<Slide> slideRet = presentation.getSlides()
                    .stream()
                    .filter((Slide::isPresenting)).findAny();

            //check slide
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.setObject(null);
                return ret;
            }
            //check existed answer
            int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            RecordId recordId = new RecordId(userId, slide.getId());

            Record record = recordRepository.findByRecordId(recordId);
            if (record != null){
                ret.setObject(null);
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

    @Override
    public ResponseObject<SlideResponse> getPresentingSlideGA02(int presentationId) {
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
            Optional<Slide> slideRet = presentation.getSlides().stream().findAny();

            //check slide
            Slide slide = slideRet.orElse(null);

            if (slide == null) {
                ret.setObject(null);
                return ret;
            }
            //check existed answer
            int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            RecordId recordId = new RecordId(userId, slide.getId());

            Record record = recordRepository.findByRecordId(recordId);
            if (record != null){
                ret.setObject(null);
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
