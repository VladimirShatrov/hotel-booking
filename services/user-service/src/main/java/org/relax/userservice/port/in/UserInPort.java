package org.relax.userservice.port.in;

import org.relax.userservice.dto.UpdateProfileRequest;
import org.relax.userservice.dto.UserData;

import java.util.UUID;

public interface UserInPort {

    UserData getUserDataById(UUID id);

    UserData updateProfile(UUID id, UpdateProfileRequest data);

    UserData save(UserData data);

    boolean existsById(UUID id);

    UserData getUserDataByEmail(String email);
}
