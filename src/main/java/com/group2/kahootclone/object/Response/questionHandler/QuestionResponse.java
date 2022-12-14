package com.group2.kahootclone.object.Response.questionHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Question;
import com.group2.kahootclone.object.Response.BaseResponse;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import lombok.Data;

@Data
public class QuestionResponse extends BaseResponse {
    int id;
    UserResponse user;
    String content;
    boolean answered;

    public static QuestionResponse fromQuestion (Question question){
        QuestionResponse response = MapperUtil.INSTANCE.map(question, QuestionResponse.class);
        response.setUser(UserResponse.fromUser(question.getUser()));
        return response;
    }

}
