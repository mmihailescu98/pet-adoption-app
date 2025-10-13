package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    List<User> getAllUsers();
}
