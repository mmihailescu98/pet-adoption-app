package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        Boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        return new LoginResponse(token, new UserLoginModel(userDetails.getId(), userDetails.getUsername(), isAdmin));
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());  // password will be encoded in UserService
        user.setName(request.getFirst_name() + " " + request.getLast_name());
        user.setEmail(request.getEmail());

        return userService.registerUser(user);
    }


    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String first_name;
        private String last_name;
        private String email;
        private String roles; // example: "ROLE_USER"
    }


    @Data
    public static class JwtResponse {
        private final String token;
    }

    @Data
    public static class LoginResponse {
        private final String token;
        private final UserLoginModel loggedUser;
    }

    @Data
    public static class UserLoginModel {
        private final Long id;
        private final String username;
        private final Boolean isAdmin;
    }
}
