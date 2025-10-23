package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="isAdmin", expression = "java(isAdmin(user))")
    UserDTO fromModelToUserDTO(User user);

    default Boolean isAdmin(User user)
    {
        return user.getRoles() != null && user.getRoles().contains("ADMIN");
    }
}
