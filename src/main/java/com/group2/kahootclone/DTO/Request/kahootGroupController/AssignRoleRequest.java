package com.group2.kahootclone.DTO.Request.kahootGroupController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignRoleRequest {
    @Min(value = 1, message = "User id is not valid")
    int userId;
    @NotBlank(message = "Role is mandatory")
    @NotNull(message = "Role is not null")
    String role;
}
