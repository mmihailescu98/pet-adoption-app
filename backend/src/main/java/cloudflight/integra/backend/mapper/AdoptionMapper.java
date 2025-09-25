package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.AdoptionListItemDTO;
import cloudflight.integra.backend.model.AdoptionEntry;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = {PetMapper.class})
public interface AdoptionMapper {
    AdoptionMapper INSTANCE = Mappers.getMapper(AdoptionMapper.class);

    /**
     * Maps an AdoptionAddRequestDTO to an Adoption from the model ,
     *  and afterward sets default values to missing values
     * @param dto Source
     * @return Target
     */
    @Mapping(target = "adopter", ignore = true)
    AdoptionEntry toModelFromAddRequest(AdoptionAddRequestDTO dto);

    /**
     * Maps an AdoptionEntry list to a AdoptionGetRequestDTO list
     * @param models list with the model entities
     * @return items for showcase of adoptions
     */
    @Mapping(target = "pet",source = "pet")//needed because it goes from model to dto
    List<AdoptionListItemDTO> toGetRequestsFromModels(List<AdoptionEntry> models);


    @AfterMapping
    default void setDefaults(@MappingTarget AdoptionEntry adotpion){
        adotpion.setCreatedAt(LocalDateTime.now());
        adotpion.setAdoptedAt(null);
        adotpion.setAdopter(null);
    }



}
