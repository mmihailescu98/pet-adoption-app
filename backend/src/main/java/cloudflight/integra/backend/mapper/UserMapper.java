package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(UserModel userModel);
    List<UserDTO> userToUserDTOList(List<UserModel> users);
}
