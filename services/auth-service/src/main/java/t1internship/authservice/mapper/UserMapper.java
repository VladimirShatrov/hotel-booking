package t1internship.authservice.mapper;

import org.mapstruct.Mapper;
import t1internship.authservice.domain.User;
import t1internship.authservice.dto.SignUpRequest;
import t1internship.authservice.dto.UserData;
import t1internship.shared.dto.UserKafkaData;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserData entityToDto(User user);
    User signUpRequestToUser(SignUpRequest request);

    UserKafkaData entityToKafkaData(User user);
}
