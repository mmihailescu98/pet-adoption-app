package cloudflight.integra.backend.service.validators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetValidator {
    static List<String> acceptedBreeds =
            Arrays.stream(PetBreedEnum.values()).map(PetBreedEnum::getCode).toList(); //aici code este fieldul din clasa PetBreedEnum, poate fi breed, sau idk cum numiti voi

    
    public static void validatePet(PetBreedEnum petBreed) {
       // you can send the whole pet as a parameter and check multiple fields

        Map<String, String> errors = new HashMap<>();

        if (!acceptedBreeds.contains(petBreed)) {
            errors.put("breed", "Breed not valid!");
        }

        if (!errors.isEmpty()) {
            throw new ValidatorException(errors.toString());
        }
    }
}

