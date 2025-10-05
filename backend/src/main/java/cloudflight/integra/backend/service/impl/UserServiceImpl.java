package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserModel updateUserProfile(Long id, UserDTO userDTO) {
        Optional<UserModel> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        UserModel user = optionalUser.get();
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
    public List<Pet> getAdoptedPetsByUser(Long id) {
        return userRepository.findByIdWithPets(id).map(user -> List.copyOf(user.getAdoptedPets())).orElse(List.of());
    }
}
