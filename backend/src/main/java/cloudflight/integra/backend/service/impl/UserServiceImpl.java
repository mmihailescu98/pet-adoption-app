package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptedPetDTO;
import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.User;
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
    public User registerUser(User user) {
        // Encode password before saving
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
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserProfile(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        User user = optionalUser.get();
        user.setUsername(userDTO.username());
        user.setName(userDTO.name());
        user.setPhone(userDTO.phone());
        user.setEmail(userDTO.email());
        user.setLocation(userDTO.location());
        user.setImgURL(userDTO.imgURL());
        user.setBio(userDTO.bio());
        return userRepository.save(user);
    }

    @Override
    public List<AdoptedPetDTO> getAdoptedPetsByUser(Long userID) {
        return userRepository.findAdoptedPetsByUserId(userID);
    }
}
