package com.group2.kahootclone.object.Request.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentSlideRequest {
    MetaData metaData;
    int slideId;
}
