package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserModel registerUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        } else {
            Set<String> normalized = user.getRoles().stream()
                    .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
            user.setRoles(normalized);
        }

        return userRepository.save(user);
    }


    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
}
