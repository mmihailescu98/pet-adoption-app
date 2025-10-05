package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findById(Long id);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.adoptedPets WHERE u.id = :id")
    Optional<UserModel> findByIdWithPets(@Param("id") Long id);
}
