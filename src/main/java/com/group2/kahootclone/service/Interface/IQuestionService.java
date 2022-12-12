package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Response.questionHandler.QuestionResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.socket.Request.questionHandler.AnswerRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AskRequest;

import java.util.List;

public interface IQuestionService {
    ResponseObject<List<QuestionResponse>> getQuestionOfPresentation(int presentationId);

    ResponseObject<QuestionResponse> answerQuestion(AnswerRequest message);

    ResponseObject<QuestionResponse> askQuestion(AskRequest message);
}
