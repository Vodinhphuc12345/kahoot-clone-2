package com.group2.kahootclone.object.Response.groupController;

import com.group2.kahootclone.Utils.LinkUtils;
import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.Invitation;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InvitationResponse extends BaseResponse {
    String invitationLink;
    String groupName;
    String code;
    int id;

    @Builder
    public InvitationResponse(long dateCreated, long dateUpdated, String link, String code, int id) {
        super(dateCreated, dateUpdated);
        this.invitationLink = link;
        this.id = id;
        this.code=code;
    }

    public static InvitationResponse fromInvitation(Invitation invitation) {
        if (invitation == null) return null;
        InvitationResponse invitationResponse = MapperUtil.INSTANCE.map(invitation, InvitationResponse.class);
        invitationResponse.setGroupName(invitation.getKahootGroup().getGroupName());
        return invitationResponse;
    }
}
