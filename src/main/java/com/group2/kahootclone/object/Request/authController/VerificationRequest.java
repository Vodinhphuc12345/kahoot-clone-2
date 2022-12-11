package com.group2.kahootclone.object.Request.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.auth.Verification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VerificationRequest {
    int verificationId;

    @Valid
    @NotBlank(message = "Verification code is mandatory")
    @NotNull(message = "Verification code is not null")
    String code;

    public Verification toVerification() {
        return MapperUtil.INSTANCE.map(this, Verification.class);
    }
}
