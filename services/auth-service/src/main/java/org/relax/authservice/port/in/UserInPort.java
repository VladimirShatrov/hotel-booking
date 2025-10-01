package org.relax.authservice.port.in;


import org.relax.authservice.dto.ChangePasswordRequest;
import org.relax.authservice.dto.UserData;

import java.util.UUID;

public interface UserInPort{

    void changePassword(ChangePasswordRequest request, UUID userId);

    void deactivateAccount(UUID userId);

    void reactivateAccount(UUID userId);

    UserData findUserByEmail(String email);

    void giveUserAdminRole(UUID userId);

}
