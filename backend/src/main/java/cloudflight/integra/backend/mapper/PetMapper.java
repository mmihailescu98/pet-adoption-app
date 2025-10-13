package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;


@Mapper
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    //normal

    @Mapping(target = "isUserFavorite", ignore = true)
    PetDTO petToPetDTO(Pet pet);

    List<PetDTO> petToPetDTOList(List<Pet> pets);

    //---------------------------------------------------


    //with isUserFavorite set

    @Named("isFavorite")
    default boolean isFavorite(Pet pet, Set<Integer> favoritePetIds) {
        return favoritePetIds.contains(pet.getId());
    }

    @Named("petToPetDTOFavorite")
    @Mapping(target = "isUserFavorite", expression = "java(isFavorite(pet, favoritePetIds))")
    PetDTO petToPetDTO(Pet pet, @Context Set<Integer> favoritePetIds);

    @IterableMapping(qualifiedByName = "petToPetDTOFavorite")
    List<PetDTO> petToPetDTOList(List<Pet> pets,@Context Set<Integer> favoritePetIds);

    //------------------------------------------------------

}
