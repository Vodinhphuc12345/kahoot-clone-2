package com.group2.kahootclone.model.group;

import com.group2.kahootclone.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserKahootGroup {
    @EmbeddedId
    private UserKahootGroupId userKahootGroupId = new UserKahootGroupId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("kahootGroupId")
    private KahootGroup kahootGroup;

    private String role;
    protected long dateCreated;
    protected long dateUpdated;

    @PreUpdate
    public void onUpdate() {
        this.setDateUpdated(System.currentTimeMillis());
    }

    @PrePersist
    public void onCreate() {
        this.setDateCreated(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "UserKahootGroup{" +
                "userKahootGroupId=" + userKahootGroupId +
                ", role='" + role + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
