package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Pet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Integer> {

    List<Pet> findPetsByName(String name);

    @Query("select p from Pet p where (:species is null or :species = '' or lower(p.species) like lower(concat('%', :species, '%'))) and (:breed is null or :breed = '' or lower(p.breed) like lower(concat('%', :breed, '%')))")
    List<Pet> filterPets(@Param("species") String species, @Param("breed") String breed, Sort sort);
}
