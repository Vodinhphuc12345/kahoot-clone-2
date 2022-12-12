package com.group2.kahootclone.socket.Request.questionHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequest {
   int questionId;
    Question toQuestion (){
        return MapperUtil.INSTANCE.map(this, Question.class);
    }
}
