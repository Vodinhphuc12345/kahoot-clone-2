package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.DTO.Response.groupController.InvitationResponse;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.DTO.ResponseObject;

public interface IInvitationService {

    ResponseObject<KahootGroupResponse> joinGroupByLink(String code, int userId);

    ResponseObject<InvitationResponse> createGroupInvitation(int groupId);

    ResponseObject<InvitationResponse> createPresentationCollaboratorInvitation(int presentationId);

    ResponseObject<PresentationResponse> joinPresentationCollaboratorPresentationByLink(String code, int userId);
}
