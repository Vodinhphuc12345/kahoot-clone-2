package com.group2.kahootclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @EmbeddedId
    private RecordId recordId = new RecordId();
    String answer;
    boolean correct;
    protected long dateCreated;
    protected long dateUpdated;

    //user
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    //slide
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("slideId")
    private Slide slide;

    @PreUpdate
    public void onUpdate() {
        this.setDateUpdated(System.currentTimeMillis());
    }

    @PrePersist
    public void onCreate() {
        this.setDateCreated(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Record{" +
                ", slide=" + slide +
                ", answer='" + answer + '\'' +
                ", correct=" + correct +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
