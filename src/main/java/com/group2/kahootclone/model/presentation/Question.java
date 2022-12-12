package com.group2.kahootclone.model.presentation;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseModel {
    String content;
    boolean answered;

    //user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    //presentation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="presentation_id")
    private Presentation presentation;

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
