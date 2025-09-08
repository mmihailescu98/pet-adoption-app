package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Integer> {

    List<Pet> findPetsByName(String name);
}
