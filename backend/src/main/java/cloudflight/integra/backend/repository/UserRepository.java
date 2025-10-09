package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> deleteByUsername(String username);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.adoptedPets WHERE u.id = :id")
    Optional<User> findByIdWithPets(@Param("id") Long id);
}
