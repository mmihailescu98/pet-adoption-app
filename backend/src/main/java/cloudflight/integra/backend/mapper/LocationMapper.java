package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.LocationDTO;
import cloudflight.integra.backend.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    List<LocationDTO> locationToLocationDTOList(List<Location> locations);
    LocationDTO locationToLocationDTO(Location location);

    Location locationDTOToLocation(LocationDTO locationDTO);
}
