package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.UserModel;

import java.util.List;
import java.util.Optional;


public interface UserService {
    UserModel registerUser(UserModel user);
    Optional<UserModel> findByUsername(String username);
    List<UserModel> getAllUsers();
}
