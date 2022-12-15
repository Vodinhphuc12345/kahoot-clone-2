package com.group2.kahootclone.object.Response.questionHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Question;
import com.group2.kahootclone.object.Response.BaseResponse;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionResponse extends BaseResponse {
    int id;
    UserResponse user;
    String content;
    boolean answered;
    int votes;

    public static QuestionResponse fromQuestion (Question question){
        QuestionResponse response = MapperUtil.INSTANCE.map(question, QuestionResponse.class);
        response.setUser(UserResponse.fromUser(question.getUser()));
        response.setVotes(question.getVotingUsers() == null ? 0 : question.getVotingUsers().size());
        return response;
    }

}
