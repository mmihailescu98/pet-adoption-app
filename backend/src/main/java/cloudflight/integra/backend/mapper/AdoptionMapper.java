package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.AdoptionListItemDTO;
import cloudflight.integra.backend.model.AdoptionEntry;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    @Mapping(target = "publisher", ignore = true)  
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "adoptedAt", ignore = true)
    AdoptionEntry toModelFromAddRequest(AdoptionAddRequestDTO dto);

    /**
     * Maps an AdoptionEntry list to a AdoptionGetRequestDTO list
     * @param models list with the model entities
     * @return items for showcase of adoptions
     */
    @Mapping(target = "pet",source = "pet")//needed because it goes from model to dto
    List<AdoptionListItemDTO> toListItemsFromModels(List<AdoptionEntry> models);


    @Mapping(target = "pet",source = "pet")
    AdoptionListItemDTO toListItemFromModel(AdoptionEntry model);


    @AfterMapping
    default void setDefaults(@MappingTarget AdoptionEntry adoption) {
        adoption.setCreatedAt(LocalDateTime.now());
        adoption.setAdoptedAt(null);
        adoption.setAdopter(null);
    }


    @Named("toListItemFromModelWithFavorites")
    @Mapping(target = "pet",source = "pet",qualifiedByName = "petToPetDTOFavorite")
    AdoptionListItemDTO toListItemFromModel(AdoptionEntry model, @Context Set<Integer> favoritePetIds);


    @IterableMapping(qualifiedByName = "toListItemFromModelWithFavorites")
    @Mapping(target = "pet",source = "pet", qualifiedByName = "petToPetDTOFavorite")
    List<AdoptionListItemDTO> toListItemsFromModels(List<AdoptionEntry> models, @Context Set<Integer> favoritePetIds);
}
