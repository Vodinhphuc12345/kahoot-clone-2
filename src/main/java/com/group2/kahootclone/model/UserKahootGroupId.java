package com.group2.kahootclone.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserKahootGroupId
        implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "kahoot_group_id")
    private int kahootGroupId;

    public UserKahootGroupId(
            int userId,
            int kahootGroupId) {
        this.userId = userId;
        this.kahootGroupId = kahootGroupId;
    }

    public UserKahootGroupId() {

    }

    //Getters omitted for brevity

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserKahootGroupId that = (UserKahootGroupId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(kahootGroupId, that.kahootGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, kahootGroupId);
    }
}

