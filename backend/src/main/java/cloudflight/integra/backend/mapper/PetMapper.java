package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.PetModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    List<PetDTO> petToPetDTOList(List<PetModel> petModels);
    PetDTO petToPetDTO(PetModel pet);
}
