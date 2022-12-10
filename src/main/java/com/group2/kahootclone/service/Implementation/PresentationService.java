package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.mapper.PresentationMapper;
import com.group2.kahootclone.model.KahootGroup;
import com.group2.kahootclone.model.Presentation;
import com.group2.kahootclone.model.User;
import com.group2.kahootclone.object.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.object.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.KahootGroupRepository;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import com.group2.kahootclone.service.Interface.IPresentationService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PresentationService implements IPresentationService {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    KahootGroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public ResponseObject<PresentationResponse> createPresentation(int userId, PresentationRequest request) {
        ResponseObject<PresentationResponse> ret = new ResponseObject<>();
        try {
            //user
            Optional<User> userRet = userRepository.findById(userId);
            User user = userRet.orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }

            //get presentation
            Presentation presentation = request.toPresentation();
            presentation.setUser(user);
            //build room
            String roomName = UUID.randomUUID().toString();
            presentation.setRoomName(roomName);
            //group
            Presentation savedPresentation = presentationRepository.save(presentation);
            //build success
            ret.setObject(PresentationResponse.fromPresentation(savedPresentation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<PresentationResponse> updatePresentation(int presentationId, PresentationRequest request) {
        ResponseObject<PresentationResponse> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //Update presentation
            Mappers.getMapper(PresentationMapper.class).presentationRequestToPresentation(request, presentation);
            //group
            Presentation savedPresentation = presentationRepository.save(presentation);
            //build success
            ret.setObject(PresentationResponse.fromPresentation(savedPresentation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<Boolean> deletePresentation(int presentationId) {
        ResponseObject<Boolean> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //delete presentation
           presentationRepository.delete(presentation);
            //build success
            ret.setObject(true);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<List<PresentationResponse>> getPresentationsOfGroup(int groupId) {
        ResponseObject<List<PresentationResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<KahootGroup> groupRet = groupRepository.findById(groupId);
            KahootGroup group = groupRet.orElse(null);

            if (group == null) {
                ret.buildResourceNotFound("Group not found.");
                return ret;
            }

            //delete presentation
            List<PresentationResponse> list = group.getPresentations()
                    .stream()
                    .map(PresentationResponse::fromPresentation)
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
    public ResponseObject<List<PresentationResponse>> getPresentationsOfUser(int userId) {
        ResponseObject<List<PresentationResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<User> userRet = userRepository.findById(userId);
            User user = userRet.orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }

            //delete presentation
            List<PresentationResponse> list = user.getPresentations()
                    .stream()
                    .map(PresentationResponse::fromPresentation)
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
    public ResponseObject<PresentationResponse> getPresentation(int presentationId) {
        ResponseObject<PresentationResponse> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //build success
            ret.setObject(PresentationResponse.fromPresentation(presentation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
