package com.group2.kahootclone.object.Request.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @NotBlank(message = "Username is mandatory")
    @NotNull(message = "Username is not null")
    @Size(min = 8, message = "Min length of username is 8")
    @Size(max = 32, message = "Max length of username is 32")
    String username;

    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "Password is not null")
    @Size(min = 8, message = "Min length of password is 8")
    @Size(max = 32, message = "Max length of password is 32")
    String password;

    public User toUser() {
        return MapperUtil.INSTANCE.map(this, User.class);
    }
}
