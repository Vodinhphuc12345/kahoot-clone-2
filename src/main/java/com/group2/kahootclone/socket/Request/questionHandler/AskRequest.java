package com.group2.kahootclone.socket.Request.questionHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AskRequest {

    int userId;
    int presentationId;
    String content;
    public Question toQuestion(){
        return MapperUtil.INSTANCE.map(this, Question.class);
    }
}
