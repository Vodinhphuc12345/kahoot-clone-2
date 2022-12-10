package com.group2.kahootclone.model;

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

    //list presentation
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "kahoot_group_presentation",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "presentation_id", referencedColumnName = "id")
    )
    List<Presentation> presentations;

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
