package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.mapper.UserMapper;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsers() {
        return UserMapper.INSTANCE.userToUserDTOList(userService.getAllUsers());
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable Long id) {
        return UserMapper.INSTANCE.userToUserDTO(userService.findById(id).orElse(null));
    }

    @GetMapping(value = "/users/{id}/pets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetDTO> getAdoptedPetsByUser(@PathVariable Long id) {
        return PetMapper.INSTANCE.petToPetDTOList(userService.getAdoptedPetsByUser(id));
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateUserProfile(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return UserMapper.INSTANCE.userToUserDTO(userService.updateUserProfile(id, userDTO));
    }
}
