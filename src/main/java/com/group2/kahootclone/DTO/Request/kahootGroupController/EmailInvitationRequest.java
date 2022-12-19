package com.group2.kahootclone.DTO.Request.kahootGroupController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailInvitationRequest {
    List<String> emails;
}
