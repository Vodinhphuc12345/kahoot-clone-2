package com.group2.kahootclone.model.auth;

import com.group2.kahootclone.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Verification extends BaseModel {
    private String code;
    private long timeExpired;
    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "Verification{" +
                "code='" + code + '\'' +
                ", timeExpired=" + timeExpired +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}