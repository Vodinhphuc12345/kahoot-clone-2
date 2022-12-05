package com.group2.kahootclone.socket.Response;

import com.group2.kahootclone.constant.socket.message.ServerMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    ServerMessageType messageType;
}
