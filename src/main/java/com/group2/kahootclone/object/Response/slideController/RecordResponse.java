package com.group2.kahootclone.object.Response.slideController;

import com.group2.kahootclone.model.presentation.record.Record;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordResponse {
    String answer;
    String user;

    public static RecordResponse fromRecord (Record record){
        return RecordResponse.builder().answer(record.getAnswer()).user(record.getUser().getDisplayName()).build();
    }
}
