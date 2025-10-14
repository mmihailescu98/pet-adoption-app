package cloudflight.integra.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;
    private String city;
    private String state;

    private Double latitude;
    private Double longitude;

    public Location() {
    }

    public Location(Integer id, String street, String city, String state, Double latitude, Double longitude) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
