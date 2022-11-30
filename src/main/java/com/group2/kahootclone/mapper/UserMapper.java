package com.group2.kahootclone.mapper;

import com.group2.kahootclone.model.User;
import com.group2.kahootclone.object.Request.authController.RegisterRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void registerRequestToUser(RegisterRequest dto, @MappingTarget User user);
}
