package com.group2.kahootclone.object.Response.slideController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class RecordResponse extends BaseResponse {
    String answer;
    String userName;
    public static RecordResponse fromRecord (Record record){
        RecordResponse response = MapperUtil.INSTANCE.map(record, RecordResponse.class);
        response.setUserName(record.getUser().getDisplayName());
        return response;
    }
}
