package com.group2.kahootclone.object.Response.chatHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Chat;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.Data;

@Data
public class ChatResponse extends BaseResponse {
    int id;
    String content;
    String userName;

    public static ChatResponse fromChat (Chat chat){
        ChatResponse response = MapperUtil.INSTANCE.map(chat, ChatResponse.class);
        response.setUserName(chat.getUser().getUsername());

        return response;
    }
}
