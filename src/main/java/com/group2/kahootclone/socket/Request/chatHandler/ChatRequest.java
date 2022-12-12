package com.group2.kahootclone.socket.Request.chatHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {
    int userId;
    int presentationId;
    String content;

    public Chat toChat (){
        return MapperUtil.INSTANCE.map(this, Chat.class);
    }
}
