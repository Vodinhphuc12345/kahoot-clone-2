package com.group2.kahootclone.object.Request.slideController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.presentation.Slide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideRequest {
    String content;
    List<String> options;
    String answer;

    public Slide toSlide() {
        return MapperUtil.INSTANCE.map(this, Slide.class);
    }
}
