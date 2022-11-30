package com.group2.kahootclone.object.Response.authController;

import com.group2.kahootclone.model.Verification;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {
    int id;
    UserResponse userResponse;
    String code;
    long timeExpired;

    public static VerificationResponse fromVerification(Verification savedVerification) {
        UserResponse userResponse1 = UserResponse.fromUser(savedVerification.getUser());
        return VerificationResponse
                .builder()
                .userResponse(userResponse1)
                .id(savedVerification.getId())
                .code(savedVerification.getCode())
                .timeExpired(savedVerification.getTimeExpired())
                .build();
    }
}
