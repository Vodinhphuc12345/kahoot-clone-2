package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.constant.ErrorCodes;
import com.group2.kahootclone.constant.ExpiredTimes;
import com.group2.kahootclone.model.Invitation;
import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.UserKahootGroup;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.object.Response.groupController.InvitationResponse;
import com.group2.kahootclone.object.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.object.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.repository.*;
import com.group2.kahootclone.service.Interface.IInvitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.group2.kahootclone.constant.Roles.MEMBER;

@Slf4j
@Service
public class InvitationService implements IInvitationService {
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KahootGroupRepository kahootGroupRepository;
    @Autowired
    UserKahootGroupRepository userKahootGroupRepository;
    @Autowired
    PresentationRepository presentationRepository;

    @Override
    public ResponseObject<KahootGroupResponse> joinGroupByLink(String code, int userId) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            Invitation invitation = invitationRepository.findByCode(code);

            //check invitation
            if (invitation == null) {
                ret.buildResourceNotFound("Invitation is not existed");
                return ret;
            }
            if (invitation.getExpiredTime() < System.currentTimeMillis()) {
                ret.setMessage("Invitation is expired");
                ret.setObject(null);
                ret.setErrorCode(ErrorCodes.EXPIRED);
                return ret;
            }

            //check user
            Optional<User> userRet = userRepository.findById(userId);
            User user = userRet.orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User is not found");
                return ret;
            }

            //check is a member of this group
            UserKahootGroup curUserKahootGroup = userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, invitation.getKahootGroup());
            if (curUserKahootGroup != null) {
                ret.setMessage("This member already joined this group");
                ret.setObject(null);
                ret.setErrorCode(ErrorCodes.EXISTED);
                return ret;
            }

            //save
            UserKahootGroup userKahootGroup = new UserKahootGroup();
            userKahootGroup.setRole(MEMBER.toString());
            userKahootGroup.setKahootGroup(invitation.getKahootGroup());
            userKahootGroup.setUser(user);
            userKahootGroupRepository.save(userKahootGroup);

            //build success
            ret.setObject(KahootGroupResponse.fromKahootGroup(invitation.getKahootGroup()));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;

    }

    @Override
    public ResponseObject<InvitationResponse> createGroupInvitation(int groupId) {
        ResponseObject<InvitationResponse> ret = new ResponseObject<>();
        try {
            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            //build resource not found
            if (kahootGroup == null) {
                ret.buildResourceNotFound("Group is not found");
                return ret;
            }

            Invitation invitation = new Invitation();
            invitation.setKahootGroup(kahootGroup);
            invitation.setExpiredTime(System.currentTimeMillis() + ExpiredTimes.INVITATION_TOKEN_EXPIRATION);
            invitation.setCode(UUID.randomUUID().toString());

            //save
            invitation = invitationRepository.save(invitation);
            //build success
            ret.setObject(InvitationResponse.fromInvitation(invitation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<InvitationResponse> createPresentationCollaboratorInvitation(int presentationId) {
        ResponseObject<InvitationResponse> ret = new ResponseObject<>();
        try {
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            //build resource not found
            if (presentation == null) {
                ret.buildResourceNotFound("Presentation is not found");
                return ret;
            }

            Invitation invitation = new Invitation();
            invitation.setPresentation(presentation);
            invitation.setExpiredTime(System.currentTimeMillis() + ExpiredTimes.INVITATION_TOKEN_EXPIRATION);
            invitation.setCode(UUID.randomUUID().toString());

            //save
            invitation = invitationRepository.save(invitation);
            //build success
            ret.setObject(InvitationResponse.fromInvitation(invitation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<PresentationResponse> joinPresentationCollaboratorPresentationByLink(String code, int userId) {
        ResponseObject<PresentationResponse> ret = new ResponseObject<>();
        try {
            Invitation invitation = invitationRepository.findByCode(code);

            //check invitation
            if (invitation == null) {
                ret.buildResourceNotFound("Invitation is not existed");
                return ret;
            }
            if (invitation.getExpiredTime() < System.currentTimeMillis()) {
                ret.setMessage("Invitation is expired");
                ret.setObject(null);
                ret.setErrorCode(ErrorCodes.EXPIRED);
                return ret;
            }

            //check user
            Optional<User> userRet = userRepository.findById(userId);
            User user = userRet.orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User is not found");
                return ret;
            }

            //check is a collaborator of this group
           Presentation curPresentation = user.getCollaboratedPresentations().stream().
                    filter(presentation -> presentation.getId() == invitation.getPresentation().getId()).findAny().orElse(null);
            if (curPresentation != null) {
                ret.setMessage("This member already joined this presentation");
                ret.setObject(null);
                ret.setErrorCode(ErrorCodes.EXISTED);
                return ret;
            }

            //save
            user.getCollaboratedPresentations().add(invitation.getPresentation());
            userRepository.save(user);

            //build success
            ret.setObject(PresentationResponse.fromPresentation(invitation.getPresentation()));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
