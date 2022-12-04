package com.group2.kahootclone.model;

import com.group2.kahootclone.constant.Providers;
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

}
