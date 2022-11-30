package com.group2.kahootclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected long dateCreated;
    protected long dateUpdated;

    @PreUpdate
    public void onUpdate() {
        this.setDateUpdated(System.currentTimeMillis());
    }

    @PrePersist
    public void onCreate() {
        this.setDateCreated(System.currentTimeMillis());
    }
}
