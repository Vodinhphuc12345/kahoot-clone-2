package com.group2.kahootclone.constant.socket.message;

public enum ClientMessageType {
    // general
    JOIN_ROOM,
    LEAVE_ROOM,
    CHAT,

    // Host
    START,
    END,
    NEXT_SLIDE,
    PREV_SLIDE,
    ANSWER_QUESTION,

    // Member
    VOTE_SLIDE,
    ASK_QUESTION
}
