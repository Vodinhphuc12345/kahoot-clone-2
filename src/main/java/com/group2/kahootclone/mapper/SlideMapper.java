package com.group2.kahootclone.mapper;

import com.group2.kahootclone.model.presentation.Slide;
import com.group2.kahootclone.DTO.Request.slideController.SlideRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SlideMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void slideRequestToSlide(SlideRequest dto, @MappingTarget Slide slide);
}
