package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.DTO.Request.kahootGroupController.AssignRoleRequest;
import com.group2.kahootclone.DTO.Request.kahootGroupController.KahootGroupRequest;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.constant.ErrorCodes;
import com.group2.kahootclone.mapper.GroupMapper;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.group.UserKahootGroup;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.repository.*;
import com.group2.kahootclone.service.Interface.IKahootGroupService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.group2.kahootclone.constant.Roles.KICK_OUT;
import static com.group2.kahootclone.constant.Roles.OWNER;

@Service
@Slf4j
public class KahootGroupService implements IKahootGroupService {

    @Autowired
    KahootGroupRepository kahootGroupRepository;
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserKahootGroupRepository userKahootGroupRepository;
    @Autowired
    PresentationRepository presentationRepository;

    @Override
    public ResponseObject<KahootGroupResponse> createKahootGroup(KahootGroupRequest request, int userId) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            //user
            User user = userRepository.findById(userId).isEmpty() ? null : userRepository.findById(userId).get();

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }

            KahootGroup kahootGroup = request.toKahootGroup();
            //build room
            String roomName = UUID.randomUUID().toString();
            kahootGroup.setRoomName(roomName);
            //group
            KahootGroup savedKahootGroup = kahootGroupRepository.save(kahootGroup);

            //user_group
            UserKahootGroup userKahootGroup = new UserKahootGroup();
            userKahootGroup.setRole(OWNER.toString());
            userKahootGroup.setKahootGroup(savedKahootGroup);
            userKahootGroup.setUser(user);
            userKahootGroupRepository.save(userKahootGroup);

            //build success
            ret.setObject(KahootGroupResponse.fromKahootGroup(savedKahootGroup));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<Boolean> deleteKahootGroup(int groupId) {
        ResponseObject<Boolean> ret = new ResponseObject<>();
        try {
            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            if (kahootGroup == null) {
                ret.buildResourceNotFound("Kahoot group is not found");
                return ret;
            }
            kahootGroupRepository.delete(kahootGroup);

            // build success
            ret.setObject(true);
            return ret;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<KahootGroupResponse> updateGroup(KahootGroupRequest kahootGroupRequest, int groupId) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            //build resource not found
            if (kahootGroup == null) {
                ret.buildResourceNotFound("Group is not found");
                return ret;
            }

            //business
            Mappers.getMapper(GroupMapper.class).groupRequestToGroup(kahootGroupRequest, kahootGroup);
            kahootGroup = kahootGroupRepository.save(kahootGroup);

            //build success
            ret.setObject(KahootGroupResponse.fromKahootGroup(kahootGroup));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<KahootGroupResponse> getKahootGroup(int groupId) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            //build resource not found
            if (kahootGroup == null) {
                ret.buildResourceNotFound("Group is not found");
                return ret;
            }

            //build success
            ret.setObject(KahootGroupResponse.fromKahootGroup(kahootGroup));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<KahootGroupResponse> assignRole(int groupId, AssignRoleRequest request) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            Optional<User> userRet = userRepository.findById(request.getUserId());
            User user = userRet.orElse(null);

            //build resource not found
            if (user == null) {
                ret.buildResourceNotFound("User is not found");
                return ret;
            }

            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            //build resource not found
            if (kahootGroup == null) {
                ret.buildResourceNotFound("Group is not found");
                return ret;
            }

            //delete role
            UserKahootGroup curUserKahootGroup = userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, kahootGroup);

            if (curUserKahootGroup != null && curUserKahootGroup.getRole().equals(OWNER.name())) {
                ret.setMessage("You can't assign role for yourself");
                ret.setErrorCode(ErrorCodes.WRITE_FAILED);
                return ret;
            }

            if (curUserKahootGroup != null) {
                userKahootGroupRepository.delete(curUserKahootGroup);
            }
            //assign role
            //user_group
            if (!Objects.equals(request.getRole(), KICK_OUT.name())) {
                UserKahootGroup userKahootGroup = new UserKahootGroup();
                userKahootGroup.setRole(request.getRole());
                userKahootGroup.setKahootGroup(kahootGroup);
                userKahootGroup.setUser(user);
                userKahootGroupRepository.save(userKahootGroup);
            }

            //build success
            ret.setObject(KahootGroupResponse.fromKahootGroup(kahootGroup));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<List<UserResponse>> getListUsersOfKahootGroup(int groupId) {
        ResponseObject<List<UserResponse>> ret = new ResponseObject<>();
        try {
            Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
            KahootGroup kahootGroup = kahootGroupRet.orElse(null);

            //build resource not found
            if (kahootGroup == null) {
                ret.buildResourceNotFound("Group is not found");
                return ret;
            }

            List<UserResponse> userResponses = kahootGroup.getUserKahootGroups().stream().map(ukp ->
            {
                UserResponse userResponse = UserResponse.fromUser(ukp.getUser());
                userResponse.setRole(ukp.getRole());
                return userResponse;
            }).collect(Collectors.toList());
            //build success
            ret.setObject(userResponses);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<KahootGroupResponse> getPresentingGroupsOfPresentation(int presentationId) {
        ResponseObject<KahootGroupResponse> ret = new ResponseObject<>();
        try {
            Presentation presentation = presentationRepository.findById(presentationId).orElse(null);

            //build resource not found
            if (presentation == null) {
                ret.buildResourceNotFound("Presentation is not found");
                return ret;
            }

            KahootGroupResponse kahootGroupResponse = KahootGroupResponse.fromKahootGroup(presentation.getPresentingGroup());
            //build success
            ret.setObject(kahootGroupResponse);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<List<KahootGroupResponse>> getKahootGroups(List<Integer> groupIds) {
        ResponseObject<List<KahootGroupResponse>> ret = new ResponseObject<>();
        try {
            List<KahootGroup> kahootGroups = kahootGroupRepository.findAllById(groupIds);
            //build resource not found
            List<KahootGroupResponse> kahootGroupResponses = kahootGroups.stream().map(KahootGroupResponse::fromKahootGroup).collect(Collectors.toList());
            //build success
            ret.setObject(kahootGroupResponses);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }


}
