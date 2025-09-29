package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.mapper.UserMapper;
import cloudflight.integra.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return UserMapper.mapper.userToUserDTOList(userService.getAllUsers());
    }

    @GetMapping("/users/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(UserMapper.mapper::userToUserDTO)
                .orElse(null);
    }
}
