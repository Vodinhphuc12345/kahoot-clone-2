package com.group2.kahootclone.object.Request.socket;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.Record;
import com.group2.kahootclone.model.RecordId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordRequest {
    MetaData metaData;
    String answer;
    int userId;
    int slideId;
    public Record toRecord() {
        RecordId recordId = new RecordId(userId, slideId);
        Record record = MapperUtil.INSTANCE.map(this, Record.class);
        record.setRecordId(recordId);

        return record;
    }
}
