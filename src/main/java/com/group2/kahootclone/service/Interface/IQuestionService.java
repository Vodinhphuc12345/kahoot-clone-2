package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.DTO.Response.questionHandler.QuestionResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.socket.Request.questionHandler.AnswerRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AskRequest;

import java.util.List;

public interface IQuestionService {
    ResponseObject<List<QuestionResponse>> getQuestionOfPresentation(int presentationId);

    ResponseObject<QuestionResponse> answerQuestion(AnswerRequest message);

    ResponseObject<QuestionResponse> askQuestion(AskRequest message);


    ResponseObject<QuestionResponse> toggleVotingQuestiontoggleQuestion(int questionId, int userId);
}
