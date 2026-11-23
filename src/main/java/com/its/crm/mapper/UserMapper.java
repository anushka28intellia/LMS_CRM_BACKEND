package com.its.crm.mapper;

import com.its.crm.dto.user.UserDTO;
import com.its.crm.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserDTO dto);

    @Mapping(target = "password", ignore = true)
    void updateEntityFromDTO(UserDTO dto, @MappingTarget UserEntity entity);
}
