package com.group2.kahootclone.model.auth;

import com.group2.kahootclone.constant.Providers;
import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.presentation.chat.Chat;
import com.group2.kahootclone.model.presentation.question.Question;
import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.model.group.UserKahootGroup;
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
public class User extends BaseModel {
    @Column (unique = true)
    private String username;
    private String password;
    private String email;
    private String displayName;
    private String avatar;
    private String provider = Providers.LOCAL.toString();
    private boolean active;

    // list group
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<UserKahootGroup> userKahootGroups;

    //list record
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Record> records;

    //list created presentation
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Presentation> presentations;

    //list questions
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Question> questions;

    //list chats
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Chat> chats;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", provider='" + provider + '\'' +
                ", active=" + active +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
