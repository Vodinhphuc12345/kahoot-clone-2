package com.group2.kahootclone.object.Response.slideController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.controller.SlideController;
import com.group2.kahootclone.model.Slide;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideResponse extends RepresentationModel<BaseResponse> {
    int id;
    String content;
    List<String> options;
    String answer;
    boolean isPresenting;
    List<RecordResponse> userRecords;

    public static SlideResponse fromSlide(Slide slide) {
        if (slide == null) return  null;
        SlideResponse response = MapperUtil.INSTANCE.map(slide, SlideResponse.class);
        Link link = linkTo(methodOn(SlideController.class).getSlide(slide.getPresentation().getId(), slide.getId())).withSelfRel();
        response.add(link);

        if (slide.getRecords() != null) {
            List<RecordResponse> recordResponses = slide.getRecords().
                    stream()
                    .map(RecordResponse::fromRecord)
                    .collect(Collectors.toList());
            response.setUserRecords(recordResponses);
        }
        return response;
    }
}
