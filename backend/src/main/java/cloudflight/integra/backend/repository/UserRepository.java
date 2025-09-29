package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findAll();
    UserModel save(UserModel model);
}
