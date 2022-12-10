package com.group2.kahootclone.controller;

import com.group2.kahootclone.Utils.EmailUtils;
import com.group2.kahootclone.Utils.ErrorUtils;
import com.group2.kahootclone.object.EmailDetails;
import com.group2.kahootclone.object.Request.kahootGroupController.AssignRoleRequest;
import com.group2.kahootclone.object.Request.kahootGroupController.EmailInvitationRequest;
import com.group2.kahootclone.object.Request.kahootGroupController.KahootGroupRequest;
import com.group2.kahootclone.object.Request.presentationController.PresentationRequest;
import com.group2.kahootclone.object.Response.groupController.InvitationResponse;
import com.group2.kahootclone.object.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import com.group2.kahootclone.object.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
public class KahootGroupController {
    @Autowired
    IKahootGroupService groupService;

    @Autowired
    IUserService userService;

    @Autowired
    IInvitationService invitationService;

    @Autowired
    IEmailService emailService;

    @Autowired
    IPresentationService presentationService;

    @Value("${kahoot.clone.fe}")
    String fehost;

    // Create group
    @PostMapping()
    public ResponseEntity<ResponseObject<KahootGroupResponse>> createGroup(@RequestBody KahootGroupRequest kahootGroupRequest) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<KahootGroupResponse> kahootGroupRet = groupService.createKahootGroup(kahootGroupRequest, userId);
        return kahootGroupRet.createResponse();
    }

    // Delete group
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId)")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseObject<Boolean>> deleteGroup(@PathVariable int groupId) {
        ResponseObject<Boolean> deleteRet = groupService.deleteKahootGroup(groupId);
        return deleteRet.createResponse();
    }

    // Update group
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) or @groupRole.isCoOwner(authentication, #groupId)")
    @PutMapping("/{groupId}")
    public ResponseEntity<ResponseObject<KahootGroupResponse>> updateGroup(@PathVariable int groupId, @RequestBody KahootGroupRequest kahootGroupRequest) {
        ResponseObject<KahootGroupResponse> kahootGroupRet = groupService.updateGroup(kahootGroupRequest, groupId);
        return kahootGroupRet.createResponse();
    }

    // Get group
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) " +
            "or @groupRole.isCoOwner(authentication, #groupId) " +
            "or @groupRole.isMember(authentication, #groupId)")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseObject<KahootGroupResponse>> getGroup(@PathVariable int groupId) {
        ResponseObject<KahootGroupResponse> kahootGroupRet = groupService.getKahootGroup(groupId);
        return kahootGroupRet.createResponse();
    }

    // Get user in group
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) " +
            "or @groupRole.isCoOwner(authentication, #groupId) " +
            "or @groupRole.isMember(authentication, #groupId)")
    @GetMapping("/{groupId}/users")
    public ResponseEntity<ResponseObject<List<UserResponse>>> getUsers(@PathVariable int groupId) {
        ResponseObject<List<UserResponse>> listUserRet = groupService.getListUsersOfKahootGroup(groupId);
        return listUserRet.createResponse();
    }

    // Add role
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) ")
    @PostMapping("/{groupId}/roleAssigment")
    public ResponseEntity<ResponseObject<KahootGroupResponse>> assignRole(@PathVariable int groupId, @Valid @RequestBody AssignRoleRequest request) {
        ResponseObject<KahootGroupResponse> kahootGroupRet = groupService.assignRole(groupId, request);
        return kahootGroupRet.createResponse();
    }

    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) or @groupRole.isCoOwner(authentication, #groupId)")
    @PostMapping("/{groupId}/invitation")
    public ResponseEntity<ResponseObject<InvitationResponse>> createInvitation(@PathVariable int groupId) {
        ResponseObject<InvitationResponse> invitationRet = invitationService.createInvitation(groupId);
        return invitationRet.createResponse();
    }

    @GetMapping("/invitation/{code}")
    public ResponseEntity<ResponseObject<KahootGroupResponse>> joinGroupByLink(@PathVariable String code) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<KahootGroupResponse> kahootGroupRet = invitationService.joinGroupByLink(code, userId);
        return kahootGroupRet.createResponse();
    }

    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) or @groupRole.isCoOwner(authentication, #groupId)")
    @PostMapping("/{groupId}/invitation/emails")
    public ResponseEntity<ResponseObject<InvitationResponse>> sendInviteEmail(@PathVariable int groupId,
                                                          @RequestBody EmailInvitationRequest emailInvitationRequest) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ResponseObject<InvitationResponse> invitationRet = invitationService.createInvitation(groupId);
        if (ErrorUtils.isFail(invitationRet.getErrorCode()))
            return invitationRet.createResponse();

        ResponseObject<UserResponse> userRet = userService.findUser(userId);
        if (ErrorUtils.isFail(userRet.getErrorCode()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        for (String email : emailInvitationRequest.getEmails()) {
            String template = EmailUtils.
                    buildInvitationTemplate(fehost, userRet.getObject().getEmail(),
                            email,
                            invitationRet.getObject().getCode(),
                            invitationRet.getObject().getGroupName());
            emailService.sendSimpleEmail(EmailDetails
                    .builder()
                    .attachment("No")
                    .subject("Invitation")
                    .recipient(email)
                    .msgBody(template)
                    .build());
        }

        return invitationRet.createResponse();
    }

    //list presentations of group
    @PreAuthorize("@groupRole.isOwner(authentication, #groupId) " +
            "or @groupRole.isCoOwner(authentication, #groupId) " +
            "or @groupRole.isMember(authentication, #groupId)")
    @GetMapping("/{groupId}/presentation")
    public ResponseEntity<ResponseObject<List<PresentationResponse>>> getPresentationsOfGroup (@PathVariable int groupId){
        ResponseObject<List<PresentationResponse>> presentationRes = presentationService.getPresentationsOfGroup(groupId);
        return presentationRes.createResponse();
    }
}
