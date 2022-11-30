package com.group2.kahootclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @OneToMany(
            mappedBy = "kahootGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<UserKahootGroup> userKahootGroups;

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
