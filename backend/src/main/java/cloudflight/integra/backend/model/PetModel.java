package cloudflight.integra.backend.model;

import java.io.Serializable;

public class PetModel implements Serializable {
    private int id;
    private String species;
    private String breed;
    private String name;
    private String location;
    private String age;
    private String description;
    private String imgURL;

    public PetModel(int id, String species, String breed, String name, String location, String age, String description, String imgURL) {
        this.id = id;
        this.species = species;
        this.breed = breed;
        this.name = name;
        this.location = location;
        this.age = age;
        this.description = description;
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
