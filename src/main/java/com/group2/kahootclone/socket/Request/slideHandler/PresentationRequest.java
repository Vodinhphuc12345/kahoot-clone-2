package com.group2.kahootclone.socket.Request.slideHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentationRequest {
    int presentationId;
    List<Integer> groupIds;
}
