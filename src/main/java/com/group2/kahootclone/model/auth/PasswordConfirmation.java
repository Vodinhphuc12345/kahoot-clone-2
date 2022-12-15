package com.group2.kahootclone.model.auth;

import com.group2.kahootclone.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class PasswordConfirmation extends BaseModel {
    String code;
    String password;
    boolean clicked;
    @ManyToOne
    private User user;
    private long timeExpired;
    @Override
    public String toString() {
        return "PasswordConfirmation{" +
                "code='" + code + '\'' +
                ", password='" + password + '\'' +
                ", clicked=" + clicked +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
