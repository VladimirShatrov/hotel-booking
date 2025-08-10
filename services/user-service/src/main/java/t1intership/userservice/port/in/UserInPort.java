package t1intership.userservice.port.in;

import t1intership.userservice.dto.UpdateProfileRequest;
import t1intership.userservice.dto.UserData;

import java.util.UUID;

public interface UserInPort {

    UserData getUserDataById(UUID id);

    UserData updateProfile(UUID id, UpdateProfileRequest data);

    UserData save(UserData data);
}
