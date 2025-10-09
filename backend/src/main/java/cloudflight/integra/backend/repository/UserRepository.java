package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> deleteByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.adoptedPets WHERE u.id = :id")
    Optional<User> findByIdWithPets(@Param("id") Long id);
}
