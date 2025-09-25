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
@Table(name = "Adoptions")
public class AdoptionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Each listing is for exactly one pet
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    // The user who owns/posted the listing
    @ManyToOne(optional = true) // for now it is optional beacuse we need the auth store on the front end ,
                                // on the final form it will be required (true)
    @JoinColumn(name = "publisher_id")
    private UserModel publisher;

    private List<String> additionalImages;
    private String contactNumber;

    // The user who adopts it (nullable until adopted)
    @ManyToOne(optional = true)
    @JoinColumn(name = "adopter_id")
    private UserModel adopter;

    private LocalDateTime createdAt;
    private LocalDateTime adoptedAt;
}

