package cloudflight.integra.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String species;
    private String breed;
    private String name;
    private String location;
    private String age;
    private String description;
    private String imgURL;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PetStatus status = PetStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    private User createdBy;

    public Pet(Integer id, String species, String breed, String name, String location,
               String age, String description, String imgURL) {
        this.id = id;
        this.species = species;
        this.breed = breed;
        this.name = name;
        this.location = location;
        this.age = age;
        this.description = description;
        this.imgURL = imgURL;
    }
}
