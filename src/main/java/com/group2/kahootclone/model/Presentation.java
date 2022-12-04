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
public class Presentation extends BaseModel{
    String presentationName;
    String description;
    String roomName;

    // user
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // group
    @ManyToOne(fetch = FetchType.LAZY)
    private KahootGroup kahootGroup;

    //list slide
    @OneToMany(
            mappedBy = "presentation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Slide> slides;

}
