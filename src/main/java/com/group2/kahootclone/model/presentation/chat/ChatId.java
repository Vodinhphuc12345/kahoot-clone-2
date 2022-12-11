package com.group2.kahootclone.model.presentation.chat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatId implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "presentation_id")
    private int presentationId;

    public ChatId(
            int userId,
            int presentationId) {
        this.userId = userId;
        this.presentationId = presentationId;
    }

    public ChatId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ChatId that = (ChatId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(presentationId, that.presentationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, presentationId);
    }
}
