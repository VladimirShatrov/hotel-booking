package t1intership.userservice.mapper;

import org.mapstruct.Mapper;
import t1intership.userservice.domain.User;
import t1intership.userservice.dto.UserData;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserData entityToDto(User user);

    public abstract User dtoToEntity(UserData data);

}
