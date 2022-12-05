package com.group2.kahootclone.socket.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketRequest<T> {
    MetaData metaData;
    T message;
}
