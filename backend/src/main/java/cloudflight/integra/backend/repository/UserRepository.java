package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.dto.AdoptedPetDTO;
import cloudflight.integra.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> deleteByUsername(String username);

    @Query("SELECT new cloudflight.integra.backend.dto.AdoptedPetDTO(" +
            "p.id, p.species, p.breed, p.name, " +
            "new cloudflight.integra.backend.dto.LocationDTO(l.id,l.state, l.city, l.street, l.latitude, l.longitude), " +
            "p.age, p.description, p.imgURL, p.status, a.createdAt, a.adoptedAt) " +
            "FROM AdoptionEntry a JOIN a.pet p JOIN p.location l " +
            "WHERE a.adopter.id = :userID AND a.adoptedAt IS NOT NULL")
    List<AdoptedPetDTO> findAdoptedPetsByUserId(@Param("userID") Long userID);

}
