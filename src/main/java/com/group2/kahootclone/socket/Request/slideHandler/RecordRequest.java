package com.group2.kahootclone.socket.Request.slideHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.model.presentation.record.RecordId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordRequest {
    int userId;
    int slideId;
    String answer;
    public Record toRecord() {
        RecordId recordId = new RecordId(userId, slideId);
        Record record = MapperUtil.INSTANCE.map(this, Record.class);
        record.setRecordId(recordId);

        return record;
    }
}
