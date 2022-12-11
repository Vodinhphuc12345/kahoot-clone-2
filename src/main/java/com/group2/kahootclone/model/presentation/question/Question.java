package com.group2.kahootclone.model.presentation.question;

import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
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
public class Question {
    @EmbeddedId
    private QuestionId questionId = new QuestionId();
    String content;
    boolean answered;
    protected long dateCreated;
    protected long dateUpdated;

    //user
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    //presentation
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("presentationId")
    private Presentation presentation;

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
        return "Question{" +
                "content='" + content + '\'' +
                ", answered=" + answered +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
