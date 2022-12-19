package com.group2.kahootclone.DTO.Request.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.auth.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotEmpty(message = "Username is mandatory")
    @Size(min = 8, message = "Min length of username is 8")
    @Size(max = 32, message = "Max length of username is 32")
    String username;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, message = "Min length of password is 8")
    @Size(max = 32, message = "Max length of password is 32")
    String password;

    @NotEmpty(message = "Display name is mandatory")
    String displayName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email;

    public User toUser() {
        return MapperUtil.INSTANCE.map(this, User.class);
    }
}
