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
public class Presentation extends BaseModel {
    String presentationName;
    String description;
    String roomName;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // group
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "presentations")
    private List<KahootGroup> kahootGroup;

    //list slide
    @OneToMany(
            mappedBy = "presentation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Slide> slides;

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
