package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.mapper.PresentationMapper;
import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Slide;
import com.group2.kahootclone.DTO.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.DTO.Response.slideController.SlideResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.repository.KahootGroupRepository;
import com.group2.kahootclone.repository.PresentationRepository;
import com.group2.kahootclone.repository.UserRepository;
import com.group2.kahootclone.service.Interface.IPresentationService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    KahootGroupRepository kahootGroupRepository;

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
    public ResponseObject<List<PresentationResponse>> getPresentedPresentationsOfGroup(int groupId) {
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
            List<PresentationResponse> list = group.getPresentedPresentations()
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
    public ResponseObject<List<PresentationResponse>> getPresentingPresentationsOfGroup(int groupId) {
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
            List<PresentationResponse> list = group.getPresentingPresentations()
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

    @Override
    public ResponseObject<List<PresentationResponse>> getCollaborationPresentationsOfUser(int userId) {
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
            List<PresentationResponse> list = user.getCollaboratedPresentations()
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
    public ResponseObject<List<UserResponse>> getCollaboratorsOfPresentation(int presentationId) {
        ResponseObject<List<UserResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //delete presentation
            List<UserResponse> list = presentation.getCollaborators()
                    .stream()
                    .map(UserResponse::fromUser)
                    .collect(Collectors.toList());
            //build success
            ret.setObject(list);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<List<SlideResponse>> startPresentation
            (com.group2.kahootclone.socket.Request.slideHandler.PresentationRequest startPresentationRequest) {
        ResponseObject<List<SlideResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(startPresentationRequest.getPresentationId());
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //end
            presentation.getSlides().forEach(slide -> {
                slide.setPresenting(false);
            });

            //start
            presentation.getSlides().get(0).setPresenting(true);
            List<KahootGroup> presentingGroups = kahootGroupRepository.findAllById(startPresentationRequest.getGroupIds());
            presentation.setPresentingGroups(presentingGroups);
            presentation.getPresentedGroups().addAll(presentingGroups);
            Presentation savedPresentation = presentationRepository.save(presentation);
            //get slides
            List<SlideResponse> list = savedPresentation.getSlides()
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

    @Transactional
    @Override
    public ResponseObject<List<SlideResponse>> endPresentation(int presentationId) {
        ResponseObject<List<SlideResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            //end
            presentation.getSlides().forEach(slide -> {
                slide.setPresenting(false);
            });
            presentation.setPresentingGroups(null);
            Presentation savedPresentation = presentationRepository.save(presentation);
            //get slides
            List<SlideResponse> list = savedPresentation.getSlides()
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

    @Transactional
    @Override
    public ResponseObject<List<SlideResponse>> nextSlide(int presentationId) {
        ResponseObject<List<SlideResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            // end
            boolean presenting = false;
            for (Slide slide : presentation.getSlides()) {
                if (presenting) {
                    slide.setPresenting(true);
                    break;
                }
                if (slide.isPresenting()) {
                    presenting = true;
                }
                slide.setPresenting(false);
            }
            Presentation savedPresentation = presentationRepository.save(presentation);
            //get slides
            List<SlideResponse> list = savedPresentation.getSlides()
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

    @Transactional
    @Override
    public ResponseObject<List<SlideResponse>> prevSlide(int presentationId) {
        ResponseObject<List<SlideResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }

            // end
            Slide prevSlide = null;
            for (Slide slide : presentation.getSlides()) {
                if (slide.isPresenting()) {
                    slide.setPresenting(false);
                    if (prevSlide != null)
                        prevSlide.setPresenting(true);
                    break;
                }
                prevSlide = slide;
            }
            Presentation savedPresentation = presentationRepository.save(presentation);
            //get slides
            List<SlideResponse> list = savedPresentation.getSlides()
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

    @Transactional
    @Override
    public ResponseObject<Object> deletePresentationCollaboration(int presentationId, int collaboratorId) {
        ResponseObject<Object> ret = new ResponseObject<>();
        try {
            //presentation
            Presentation presentation = presentationRepository.findById(presentationId).orElse(null);
            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            // collanorations
            User user = userRepository.findById(collaboratorId).orElse(null);
            if (user == null) {
                ret.buildResourceNotFound("Collaborator not found.");
                return ret;
            }
            // check relation
            User collaborator = presentation.getCollaborators().stream().filter(c -> c.getId() == user.getId()).findAny().orElse(null);
            if (collaborator == null){
                ret.buildResourceNotFound("User is not collaborator of this group.");
                return ret;
            }
            // end
            user.getCollaboratedPresentations().remove(presentation);
            userRepository.save(user);
            //build success
            ret.setObject(null);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
