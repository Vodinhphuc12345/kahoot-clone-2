package com.group2.kahootclone.model.presentation;

import com.group2.kahootclone.model.BaseModel;
import com.group2.kahootclone.model.presentation.record.Record;
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
    String type;

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
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
