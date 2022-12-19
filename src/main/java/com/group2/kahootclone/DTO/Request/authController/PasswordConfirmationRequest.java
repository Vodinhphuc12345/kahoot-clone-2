package com.group2.kahootclone.DTO.Request.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.constant.ExpiredTimes;
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
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordConfirmationRequest {
    @Valid
    @NotBlank(message = "Password confirmation password is mandatory")
    @NotNull(message = "Password confirmation password is not null")
    @Size(min = 8, message = "Min length of username is 8")
    @Size(max = 32, message = "Max length of username is 32")
    String password;

    @Valid
    @NotBlank(message = "Password confirmation code is mandatory")
    @NotNull(message = "Password confirmation code is not null")
    @Size(min = 8, message = "Min length of username is 8")
    @Size(max = 32, message = "Max length of username is 32")
    String username;

    @Valid
    @NotBlank(message = "Password confirmation email is mandatory")
    @NotNull(message = "Password confirmation email is not null")
    @Email(message = "Email is not valid")
    String email;

    public PasswordConfirmation toPasswordConfirmation() {
        PasswordConfirmation passwordConfirmation = MapperUtil.INSTANCE.map(this, PasswordConfirmation.class);
        passwordConfirmation.setCode(UUID.randomUUID().toString());
        passwordConfirmation.setTimeExpired(System.currentTimeMillis()+ ExpiredTimes.INVITATION_TOKEN_EXPIRATION);
        return passwordConfirmation;
    }
}
