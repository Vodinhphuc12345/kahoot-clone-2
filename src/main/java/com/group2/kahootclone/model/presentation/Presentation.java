package com.group2.kahootclone.model.presentation;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.presentation.chat.Chat;
import com.group2.kahootclone.model.presentation.question.Question;
import com.group2.kahootclone.model.presentation.record.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Presentation extends BaseModel {
    String presentationName;
    String description;
    String roomName;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // presenting group
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "presentingPresentations")
    private List<KahootGroup> presentingGroups;

    // presented group
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "presentedPresentations")
    private List<KahootGroup> presentedGroups;

    //list slide
    @OneToMany(
            mappedBy = "presentation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Slide> slides;

    //list questions
    @OneToMany(
            mappedBy = "presentation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Question> questions;

    //list chats
    @OneToMany(
            mappedBy = "presentation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Chat> chats;

    @Override
    public String toString() {
        return "Presentation{" +
                "presentationName='" + presentationName + '\'' +
                ", description='" + description + '\'' +
                ", roomName='" + roomName + '\'' +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
