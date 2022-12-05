package com.group2.kahootclone.socket.Request.slideHandler;

import com.group2.kahootclone.socket.Request.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentSlideRequest {
    int slideId;
}
