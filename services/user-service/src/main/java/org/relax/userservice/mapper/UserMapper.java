package org.relax.userservice.mapper;

import org.mapstruct.Mapper;
import org.relax.userservice.domain.User;
import org.relax.userservice.dto.UserData;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserData entityToDto(User user);

    public abstract User dtoToEntity(UserData data);

}
