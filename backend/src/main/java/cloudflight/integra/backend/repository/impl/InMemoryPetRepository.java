package cloudflight.integra.backend.repository.impl;

import cloudflight.integra.backend.model.PetModel;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.exception.RepositoryException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryPetRepository implements PetRepository {
    private final List<PetModel> pets = new ArrayList<>();

    public InMemoryPetRepository() {
        PetModel dummyPet = new PetModel(1, "Dog", "Rotweiler", "Rex", "Cluj-Napoca", "1 Year and 3 months", "A very friendly dog", "https://example.com/dog.jpg");
        pets.add(dummyPet);
    }

    @Override
    public PetModel savePet(PetModel petModel) {
        if (getPetById(petModel.getId()).isPresent()) {
            throw new RepositoryException("Pet with id " + petModel.getId() + " already exists");
        }
        pets.add(petModel);
        return petModel;
    }

    @Override
    public Optional<PetModel> getPetById(int id){
        return pets.stream().filter(petModel -> petModel.getId() == id).findFirst();
    }

    @Override
    public Optional<PetModel> getPetByName(String name){
        return pets.stream().filter(petModel -> petModel.getName().equals(name)).findFirst();
    }

    @Override
    public List<PetModel> getAllPets(){
        return pets;
    }

    @Override
    public void deletePet(PetModel petModel) {
        Optional<PetModel> optionalPet = getPetById(petModel.getId());
        if (optionalPet.isPresent()) {
            pets.remove(optionalPet.get());
        } else {
            throw new RepositoryException("Pet with id " + petModel.getId() + " does not exist");
        }
    }

    @Override
    public void updatePet(PetModel petModel) {
        Optional<PetModel> existingPet = getPetById(petModel.getId());
        if (existingPet.isPresent()) {
            pets.remove(existingPet.get());
            pets.add(petModel);
        } else {
            throw new RepositoryException("Pet with id " + petModel.getId() + " does not exist");
        }
    }
}
