package com.group2.kahootclone.DTO.Response.presentationController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.controller.PresentationController;
import com.group2.kahootclone.model.presentation.Presentation;
import com.group2.kahootclone.DTO.Response.BaseResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import lombok.*;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@NoArgsConstructor
public class PresentationResponse extends BaseResponse {
    int id;
    String presentationName;
    String roomName;
    String description;
    UserResponse owner;


    public static PresentationResponse fromPresentation(Presentation presentation) {
        PresentationResponse response = MapperUtil.INSTANCE.map(presentation, PresentationResponse.class);
        response.setOwner(UserResponse.fromUser(presentation.getUser()));
        Link link = linkTo(methodOn(PresentationController.class).getPresentation(presentation.getId())).withSelfRel();
        response.add(link);
        return response;
    }
}
