package com.group2.kahootclone.socket.Request.slideHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.Record;
import com.group2.kahootclone.model.RecordId;
import com.group2.kahootclone.socket.Request.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordRequest {
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
