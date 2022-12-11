package com.group2.kahootclone.model.presentation.question;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuestionId implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "presentation_id")
    private int presentationId;

    public QuestionId(
            int userId,
            int presentationId) {
        this.userId = userId;
        this.presentationId = presentationId;
    }

    public QuestionId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        QuestionId that = (QuestionId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(presentationId, that.presentationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, presentationId);
    }
}
