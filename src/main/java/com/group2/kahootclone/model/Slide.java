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
public class Slide extends BaseModel {
    String content;
    String answer;

    boolean isPresenting;

    //options
    @ElementCollection
    List<String> options;

    //presentation
    @ManyToOne(fetch = FetchType.LAZY)
    private Presentation presentation;

    //list record
    @OneToMany(
            mappedBy = "slide",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Record> records;

    @Override
    public String toString() {
        return "Slide{" +
                "content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", isPresenting=" + isPresenting +
                ", presentation=" + presentation +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
