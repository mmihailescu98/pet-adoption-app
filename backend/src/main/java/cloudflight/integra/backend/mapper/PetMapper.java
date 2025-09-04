package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.PetModel;

public class PetMapper {
    public static PetDTO petDto(PetModel pet) {
        return new PetDTO(pet.getId(), pet.getSpecies(), pet.getBreed(), pet.getName(), pet.getLocation(), pet.getAge(), pet.getDescription(),  pet.getImgURL());
    }
}
