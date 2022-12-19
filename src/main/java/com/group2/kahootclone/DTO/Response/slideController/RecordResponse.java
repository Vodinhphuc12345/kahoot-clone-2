package com.group2.kahootclone.DTO.Response.slideController;

import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.record.Record;
import com.group2.kahootclone.DTO.Response.BaseResponse;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class RecordResponse extends BaseResponse {
    String answer;
    UserResponse user;
    public static RecordResponse fromRecord (Record record){
        RecordResponse response = MapperUtil.INSTANCE.map(record, RecordResponse.class);
        response.setUser(UserResponse.fromUser(record.getUser()));
        return response;
    }
}
