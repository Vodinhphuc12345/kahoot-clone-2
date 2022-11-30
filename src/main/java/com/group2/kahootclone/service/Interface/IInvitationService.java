package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Response.groupController.InvitationResponse;
import com.group2.kahootclone.object.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.object.ResponseObject;

public interface IInvitationService {

    ResponseObject<KahootGroupResponse> joinGroupByLink(String code, int userId);

    ResponseObject<InvitationResponse> createInvitation(int groupId);
}
