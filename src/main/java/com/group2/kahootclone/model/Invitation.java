package com.group2.kahootclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Invitation extends BaseModel{
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kahoot_group_id")
    private KahootGroup kahootGroup;
    private long expiredTime;
}
