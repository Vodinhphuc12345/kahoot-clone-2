package com.group2.kahootclone.socket.Request.questionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToggleVotingQuestionRequest {
    int userId;
    int questionId;
}
