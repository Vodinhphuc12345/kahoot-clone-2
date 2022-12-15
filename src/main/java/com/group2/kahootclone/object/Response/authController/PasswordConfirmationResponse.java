package com.group2.kahootclone.object.Response.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.auth.PasswordConfirmation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordConfirmationResponse {
   String code;
    String email;
    public static PasswordConfirmationResponse fromPasswordConfirmation(PasswordConfirmation passwordConfirmation) {
        return MapperUtil.INSTANCE.map(passwordConfirmation, PasswordConfirmationResponse.class);
    }
}
