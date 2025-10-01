package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return new JwtResponse(token);
    }

    @PostMapping("/register")
    public UserModel register(@RequestBody RegisterRequest request) {
        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());  // password will be encoded in UserService

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
        private String roles; // example: "ROLE_USER"
    }


    @Data
    public static class JwtResponse {
        private final String token;
    }
}
