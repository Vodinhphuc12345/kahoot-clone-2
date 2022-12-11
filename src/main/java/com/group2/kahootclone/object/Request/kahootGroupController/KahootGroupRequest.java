package com.group2.kahootclone.object.Request.kahootGroupController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.group.KahootGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KahootGroupRequest {
    @NotBlank(message = "Group Name is mandatory")
    @NotNull(message = "Group name is not null")
    private String groupName;
    @NotBlank(message = "Description is mandatory")
    @NotNull(message = "Description is not null")
    private String description;
    public KahootGroup toKahootGroup() {
        return MapperUtil.INSTANCE.map(this, KahootGroup.class);
    }
}
