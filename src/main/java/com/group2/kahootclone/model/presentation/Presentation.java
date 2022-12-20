package com.group2.kahootclone.model.presentation;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.Invitation;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.KahootGroup;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "kahoot_group_id")
    private KahootGroup presentingGroup;

    // presented group
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "presented_presentation_groups",
            joinColumns = @JoinColumn(name = "presentation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id")
    )
    private Set<KahootGroup> presentedGroups;

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

    // collaborators
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "collaboratedPresentations")
    private Set<User> collaborators;

    @PreRemove
    void removeSth (){
        for (User user: collaborators){
            user.getCollaboratedPresentations().remove(this);
        }
    }

    // list collaboration invitations
    @OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL)
    List<Invitation> invitations;

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
