package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(uses = LocationMapper.class)
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

//    @Mapping(target = "location",source = "location")
    List<PetDTO> petToPetDTOList(List<Pet> pets);
    PetDTO petToPetDTO(Pet pet);

    Pet petDTOToPet(PetDTO petDTO);
}
