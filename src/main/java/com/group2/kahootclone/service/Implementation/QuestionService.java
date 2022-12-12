package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.model.presentation.Question;
import com.group2.kahootclone.object.Response.questionHandler.QuestionResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.QuestionRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import com.group2.kahootclone.service.Interface.IQuestionService;
import com.group2.kahootclone.socket.Request.questionHandler.AnswerRequest;
import com.group2.kahootclone.socket.Request.questionHandler.AskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService implements IQuestionService {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Transactional
    @Override
    public ResponseObject<List<QuestionResponse>> getQuestionOfPresentation(int presentationId) {
        ResponseObject<List<QuestionResponse>> ret = new ResponseObject<>();
        try {
            //group
            Optional<Presentation> presentationRet = presentationRepository.findById(presentationId);
            Presentation presentation = presentationRet.orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //get chats
            List<QuestionResponse> list = presentation.getQuestions()
                    .stream()
                    .map(QuestionResponse::fromQuestion)
                    .collect(Collectors.toList());
            //build success
            ret.setObject(list);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Transactional
    @Override
    public ResponseObject<QuestionResponse> answerQuestion(AnswerRequest message) {
        ResponseObject<QuestionResponse> ret = new ResponseObject<>();
        try {
            //question
            Question question = questionRepository.findById(message.getQuestionId()).orElse(null);

            if (question == null) {
                ret.buildResourceNotFound("Question not found.");
                return ret;
            }
            //set
            question.setAnswered(true);
            //save
            Question savedQuestion = questionRepository.save(question);
            //build success
            ret.setObject(QuestionResponse.fromQuestion(savedQuestion));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<QuestionResponse> askQuestion(AskRequest message) {
        ResponseObject<QuestionResponse> ret = new ResponseObject<>();
        try {
            //presentation
            Presentation presentation = presentationRepository.findById(message.getPresentationId()).orElse(null);

            if (presentation == null) {
                ret.buildResourceNotFound("Presentation not found.");
                return ret;
            }
            //user
            User user = userRepository.findById(message.getUserId()).orElse(null);

            if (user == null) {
                ret.buildResourceNotFound("User not found.");
                return ret;
            }

            //set
            Question question = message.toQuestion();
            question.setUser(user);
            question.setPresentation(presentation);

            //save
            Question savedQuestion = questionRepository.save(question);
            //build success
            ret.setObject(QuestionResponse.fromQuestion(savedQuestion));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
