package com.group2.kahootclone.mapper;

import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.DTO.Request.kahootGroupController.KahootGroupRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void groupRequestToGroup(KahootGroupRequest dto, @MappingTarget KahootGroup kahootGroup);
}
