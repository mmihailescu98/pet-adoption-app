package cloudflight.integra.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "adoptions")
public class  AdoptionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each listing is for exactly one pet
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // The user who owns/posted the listing
    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    private List<String> additionalImages;
    private String contactNumber;

    // The user who adopts it (nullable until adopted)
    @ManyToOne(optional = true)
    @JoinColumn(name = "adopter_id")
    private User adopter;

    private LocalDateTime createdAt;
    private LocalDateTime adoptedAt;
}

