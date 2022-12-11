package com.group2.kahootclone.object.Response.meController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse extends BaseResponse {

    String username;
    String displayName;
    String email;
    String provider;
    boolean active;
    String role;
    String avatar;
    int id;

    @Builder
    public UserResponse(long dateCreated, long dateUpdated, String username, String displayName, String email, String provider, boolean active, String role, String avatar, int id) {
        super(dateCreated, dateUpdated);
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.provider = provider;
        this.active = active;
        this.role = role;
        this.avatar=avatar;
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", provider='" + provider + '\'' +
                ", active=" + active +
                ", role='" + role + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }

    public static UserResponse fromUser(User user){
        return MapperUtil.INSTANCE.map(user, UserResponse.class);
    }
}
