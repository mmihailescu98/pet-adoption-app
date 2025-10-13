package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.FavoritePet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePetRepository extends JpaRepository<FavoritePet, Long> {
    List<FavoritePet> findByUser_Id(Long userId);
    Optional<FavoritePet> findByUser_IdAndPet_Id(Long userId, Integer petId);
    int deleteByUser_IdAndPet_Id(Long userId, Integer petId);
}
