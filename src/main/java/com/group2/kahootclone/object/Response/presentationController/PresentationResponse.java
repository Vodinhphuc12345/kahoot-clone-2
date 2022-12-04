package com.group2.kahootclone.object.Response.presentationController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.controller.KahootGroupController;
import com.group2.kahootclone.controller.PresentationController;
import com.group2.kahootclone.model.Presentation;
import com.group2.kahootclone.object.Response.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@NoArgsConstructor
public class PresentationResponse extends BaseResponse  {
    int id;
    String presentationName;
    String roomName;
    String description;

    @Builder
    public PresentationResponse(int id, String presentationName, String description, String roomName, long dateCreated, long dateUpdated) {
        super(dateCreated, dateUpdated);
        this.id = id;
        this.presentationName = presentationName;
        this.description = description;
        this.roomName = roomName;
    }

    public static PresentationResponse fromPresentation(Presentation presentation) {
        PresentationResponse response = MapperUtil.INSTANCE.map(presentation, PresentationResponse.class);
        Link link = linkTo(methodOn(PresentationController.class).getPresentation(presentation.getId())).withSelfRel();
        response.add(link);

        return response;
    }
}
