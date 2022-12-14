package com.group2.kahootclone.model.group;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.Invitation;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
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
public class KahootGroup extends BaseModel {
    private String groupName;
    private String description;
    //list user
    @OneToMany(
            mappedBy = "kahootGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<UserKahootGroup> userKahootGroups;

    //list presenting presentation
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "presentingGroups")
    List<Presentation> presentingPresentations;

    //list presented presentation
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "presentedGroups")
    List<Presentation> presentedPresentations;

    // remove presentation from group before remove this
    @PreRemove
    private void removePresentationFromGroup() {
        for (Presentation p : presentedPresentations) {
            p.getPresentedGroups().remove(this);
        }
        for (Presentation p : presentingPresentations) {
            p.getPresentingGroups().remove(this);
        }
    }

    // list invitation
    @OneToMany(mappedBy = "kahootGroup", cascade = CascadeType.ALL)
    List<Invitation> invitations;


    @Override
    public String toString() {
        return "KahootGroup{" +
                "groupName='" + groupName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
