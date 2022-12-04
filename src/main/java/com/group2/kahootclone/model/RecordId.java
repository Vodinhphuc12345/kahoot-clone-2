package com.group2.kahootclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecordId implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "slide_id")
    private int slideId;

    public RecordId(
            int userId,
            int slideId) {
        this.userId = userId;
        this.slideId = slideId;
    }

    public RecordId() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        RecordId that = (RecordId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(slideId, that.slideId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, slideId);
    }
}
