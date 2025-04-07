package com.planet.reservation.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    UserDTO toDTO(User user);

    User toModel(UserDTO userDTO);
}
