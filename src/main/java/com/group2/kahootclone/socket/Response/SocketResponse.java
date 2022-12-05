package com.group2.kahootclone.socket.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketResponse<T> {
    MetaData metaData;
    T message;
}
