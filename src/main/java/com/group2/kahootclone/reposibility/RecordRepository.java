package com.group2.kahootclone.reposibility;

import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.model.presentation.record.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    Record findByRecordId(RecordId recordId);
}
