package com.group2.kahootclone.model.group;

import com.group2.kahootclone.model.BaseModel;
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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "presenting_presentation_groups",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "presentation_id", referencedColumnName = "id")
    )
    List<Presentation> presentingPresentations;

    //list presented presentation
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "presented_presentation_groups",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "presentation_id", referencedColumnName = "id")
    )
    List<Presentation> presentedPresentations;

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