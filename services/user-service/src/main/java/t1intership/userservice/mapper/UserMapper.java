package t1intership.userservice.mapper;

import org.mapstruct.Mapper;
import t1intership.userservice.domain.User;
import t1intership.userservice.dto.UpdateProfileRequest;
import t1intership.userservice.dto.UserData;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserData entityToDto(User user);
    UserData updateRequestToDTO(UpdateProfileRequest request);

    User dtoToEntity(UserData data);
}
