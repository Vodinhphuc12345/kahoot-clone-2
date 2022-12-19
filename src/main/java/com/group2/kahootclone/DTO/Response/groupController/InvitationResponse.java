package com.group2.kahootclone.DTO.Response.groupController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.Invitation;
import com.group2.kahootclone.DTO.Response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InvitationResponse extends BaseResponse {
    String invitationLink;
    String groupName;
    String _presentationName;
    String code;
    int id;

    @Builder
    public InvitationResponse(long dateCreated, long dateUpdated, String link, String code, int id, String groupName, String presentationName) {
        super(dateCreated, dateUpdated);
        this.invitationLink = link;
        this.id = id;
        this.code = code;
        this.groupName=groupName;
        this._presentationName=presentationName;
    }

    public static InvitationResponse fromInvitation(Invitation invitation) {
        if (invitation == null) return null;
        InvitationResponse invitationResponse = MapperUtil.INSTANCE.map(invitation, InvitationResponse.class);
        if (invitation.getKahootGroup() != null){
            invitationResponse.setGroupName(invitation.getKahootGroup().getGroupName());
        }
        if (invitation.getPresentation() != null){
            invitationResponse.set_presentationName(invitation.getPresentation().getPresentationName());
        }
        return invitationResponse;
    }
}
