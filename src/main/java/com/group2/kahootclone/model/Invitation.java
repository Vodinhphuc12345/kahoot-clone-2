package com.group2.kahootclone.model;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.presentation.Presentation;
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
public class Invitation extends BaseModel {
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kahoot_group_id")
    private KahootGroup kahootGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="presentation_id")
    private Presentation presentation;
    private long expiredTime;

    @Override
    public String toString() {
        return "Invitation{" +
                "code='" + code + '\'' +
                ", expiredTime=" + expiredTime +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
