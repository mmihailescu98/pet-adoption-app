package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.FavoritePet;
import cloudflight.integra.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FavoritePetRepository extends JpaRepository<FavoritePet, Long> {
    Optional<FavoritePet> findByUser_IdAndPet_Id(Long userId, Integer petId);
    int deleteByUser_IdAndPet_Id(Long userId, Integer petId);

    @Query("SELECT f.pet FROM FavoritePet f WHERE f.user.id = :userId")
    List<Pet> findPetsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.pet.id FROM FavoritePet f WHERE f.user.id = :userId")
    Set<Integer> findPetIdsByUserId(@Param("userId") Long userId);
}
