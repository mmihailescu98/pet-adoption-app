package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.model.AdoptionEntry;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface AdoptionMapper {
    AdoptionMapper INSTANCE = Mappers.getMapper(AdoptionMapper.class);

    /**
     * Maps an AdoptionListingAddRequestDTO to a AdoptionListing from the model ,
     *  and afterward sets default values to missing values
     * @param dto Source
     * @return Target
     */
    @Mapping(target = "adopter", ignore = true)
    AdoptionEntry toModelFromAddRequest(AdoptionAddRequestDTO dto);

    @AfterMapping
    default void setDefaults(@MappingTarget AdoptionEntry listing){
        listing.setCreatedAt(LocalDateTime.now());
        listing.setAdopter(null);
    }

}
