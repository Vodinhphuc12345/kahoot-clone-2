package com.group2.kahootclone.DTO.Response.chatHandler;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Chat;
import com.group2.kahootclone.DTO.Response.BaseResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import lombok.Data;

@Data
public class ChatResponse extends BaseResponse {
    int id;
    String content;
    UserResponse user;

    public static ChatResponse fromChat (Chat chat){
        ChatResponse response = MapperUtil.INSTANCE.map(chat, ChatResponse.class);
        response.setUser(UserResponse.fromUser(chat.getUser()));

        return response;
    }
}
