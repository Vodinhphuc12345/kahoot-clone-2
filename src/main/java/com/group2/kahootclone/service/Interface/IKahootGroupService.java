package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.DTO.Request.kahootGroupController.AssignRoleRequest;
import com.group2.kahootclone.DTO.Request.kahootGroupController.KahootGroupRequest;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.ResponseObject;

import java.util.List;

public interface IKahootGroupService {
    ResponseObject<KahootGroupResponse> createKahootGroup (KahootGroupRequest request, int userId);

    ResponseObject<Boolean> deleteKahootGroup(int groupId);

    ResponseObject<KahootGroupResponse> updateGroup(KahootGroupRequest kahootGroupRequest, int groupId);

    ResponseObject<KahootGroupResponse> getKahootGroup(int groupId);

    ResponseObject<KahootGroupResponse> assignRole(int groupId, AssignRoleRequest request);

    ResponseObject<List<UserResponse>> getListUsersOfKahootGroup(int groupId);

    ResponseObject<List<KahootGroupResponse>> getPresentingGroupsOfPresentation(int presentationId);

    ResponseObject<List<KahootGroupResponse>> getKahootGroups(List<Integer> groupIds);
}
