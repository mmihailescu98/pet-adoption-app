package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.impl.UserServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return new LoginResponse(token, new UserLoginModel(userDetails.getId(), userDetails.getUsername()));
    }

    @PostMapping("/register")
    public UserModel register(@RequestBody RegisterRequest request) {
        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());  // password will be encoded in UserService

        return userServiceImpl.registerUser(user);
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
    }
}
