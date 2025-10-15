package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User userModel);
    List<UserDTO> userToUserDTOList(List<User> users);
}
