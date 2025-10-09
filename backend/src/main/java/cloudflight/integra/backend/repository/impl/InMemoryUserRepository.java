package cloudflight.integra.backend.repository.impl;

import cloudflight.integra.backend.model.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class InMemoryUserRepository {

    private final Map<Long, UserModel> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public InMemoryUserRepository() {
        save(UserModel.builder()
                .username("admin")
                .password("admin123") // will be encoded
                .roles(Set.of("ROLE_ADMIN", "ROLE_USER"))
                .build());

        save(UserModel.builder()
                .username("user")
                .password("parola111") // will be encoded
                .roles(Set.of("ROLE_USER"))
                .build());
    }

    //@Override i commented this because now it does not extend the actual repository , since i made it to extend JPARepository
    public Optional<UserModel> findByUsername(String username) {
        return users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    //@Override
    public List<UserModel> findAll() {
        return new ArrayList<>(users.values());
    }

    //@Override
    public UserModel save(UserModel user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password
        users.put(user.getId(), user);
        return user;
    }
}
