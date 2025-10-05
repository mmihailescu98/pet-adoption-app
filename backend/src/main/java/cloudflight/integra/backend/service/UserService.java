package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;

import java.util.List;
import java.util.Optional;


public interface UserService {
    UserModel registerUser(UserModel user);
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findById(Long id);
    List<UserModel> getAllUsers();

    UserModel updateUserProfile(Long id, UserDTO userDTO);
    List<Pet> getAdoptedPetsByUser(Long id);
}
