package org.relax.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.relax.userservice.domain.User;
import org.relax.userservice.dto.UpdateProfileRequest;
import org.relax.userservice.dto.UserData;
import org.relax.userservice.mapper.UserMapper;
import org.relax.userservice.repository.UserRepository;
import org.relax.userservice.port.in.UserInPort;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserInPort {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserData getUserDataById(UUID id) {
        return userRepository.findById(id).map(mapper::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));
    }


    @Override
    @Transactional
    public UserData updateProfile(UUID id, UpdateProfileRequest data) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));

        if (data.firstName() != null) {
            user.setFirstName(data.firstName());
        }
        if (data.lastName() != null) {
            user.setLastName(data.lastName());
        }
        if (data.username() != null) {
            user.setUsername(data.username());
        }

        User updatedUser = userRepository.save(user);

        return mapper.entityToDto(updatedUser);
    }

    @Override
    @Transactional
    public UserData save(UserData data) {
        User user = mapper.dtoToEntity(data);
        this.userRepository.save(user);
        return data;
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserData getUserDataByEmail(String email) {
        return mapper.entityToDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email: " + email + " не найден")));
    }
}
