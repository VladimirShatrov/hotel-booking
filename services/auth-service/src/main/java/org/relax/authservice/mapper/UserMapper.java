package org.relax.authservice.mapper;

import org.mapstruct.Mapper;
import org.relax.authservice.domain.User;
import org.relax.authservice.dto.SignUpRequest;
import org.relax.authservice.dto.UserData;
import org.relax.shared.dto.UserKafkaData;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserData entityToDto(User user);
    User signUpRequestToUser(SignUpRequest request);

    UserKafkaData entityToKafkaData(User user);
}
