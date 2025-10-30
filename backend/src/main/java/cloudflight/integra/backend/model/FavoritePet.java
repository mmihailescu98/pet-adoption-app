package cloudflight.integra.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(
    name = "favorite_pets",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "pet_id"})
    }
)
public class FavoritePet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many favorites belong to one user
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many favorites belong to one pet
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}

