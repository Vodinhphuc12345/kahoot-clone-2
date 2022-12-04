package com.group2.kahootclone.mapper;

import com.group2.kahootclone.model.Presentation;
import com.group2.kahootclone.object.Request.presentationController.PresentationRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PresentationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void presentationRequestToPresentation(PresentationRequest dto, @MappingTarget Presentation presentation);
}
