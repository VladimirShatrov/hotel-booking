package t1internship.authservice.port.in;


import org.springframework.security.core.userdetails.UserDetailsService;
import t1internship.authservice.dto.ChangePasswordRequest;
import t1internship.authservice.dto.UserData;

import java.util.UUID;

public interface UserInPort{

    void changePassword(ChangePasswordRequest request, UUID userId);

    void deactivateAccount(UUID userId);

    void reactivateAccount(UUID userId);

    UserData findUserByEmail(String email);

}
