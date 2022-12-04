package com.group2.kahootclone.reposibility;

import com.group2.kahootclone.model.Record;
import com.group2.kahootclone.model.RecordId;
import com.group2.kahootclone.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    Record findByRecordId(RecordId recordId);
}
