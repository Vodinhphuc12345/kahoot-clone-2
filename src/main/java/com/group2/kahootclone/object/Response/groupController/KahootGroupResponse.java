package com.group2.kahootclone.object.Response.groupController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.constant.Roles;
import com.group2.kahootclone.controller.KahootGroupController;
import com.group2.kahootclone.model.KahootGroup;
import com.group2.kahootclone.model.UserKahootGroup;
import com.group2.kahootclone.object.Response.BaseResponse;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@NoArgsConstructor
public class KahootGroupResponse extends BaseResponse {
    private String groupName;
    private String description;
    private String role;
    private UserResponse owner;
    private  int id;


    @Builder
    public KahootGroupResponse(long dateCreated, long dateUpdated, String groupName, String role, String description, UserResponse owner, int id) {
        super(dateCreated, dateUpdated);
        this.groupName = groupName;
        this.role = role;
        this.description = description;
        this.owner = owner;
        this.id = id;
    }

    public static KahootGroupResponse fromKahootGroup(KahootGroup kahootGroup) {
        KahootGroupResponse response = MapperUtil.INSTANCE.map(kahootGroup, KahootGroupResponse.class);

        List<UserKahootGroup> userKahootGroups = kahootGroup.getUserKahootGroups();
        if (userKahootGroups != null) {
            UserKahootGroup filteredKahootGroup =
                    kahootGroup.getUserKahootGroups()
                            .stream()
                            .filter(ukp -> ukp.getRole().equals(Roles.OWNER.name()))
                            .findAny()
                            .orElse(null);
            if (filteredKahootGroup != null && filteredKahootGroup.getUser() != null) {
                response.setOwner(UserResponse.fromUser(filteredKahootGroup.getUser()));
            }
        }
        Link link = linkTo(methodOn(KahootGroupController.class).getGroup(kahootGroup.getId())).withSelfRel();
        response.add(link);
        return response;
    }
}
