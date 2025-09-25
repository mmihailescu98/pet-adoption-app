package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.UserModel;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findAll();
    UserModel save(UserModel model);
}
