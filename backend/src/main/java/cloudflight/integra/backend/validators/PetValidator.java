package cloudflight.integra.backend.validators;

import cloudflight.integra.backend.model.Pet;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PetValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Pet.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Pet pet = (Pet) target;

        // Name check
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            errors.rejectValue("name", "name.empty", "Name cannot be empty");
        }

        // Species check
        if (pet.getSpecies() == null || pet.getSpecies().trim().isEmpty()) {
            errors.rejectValue("species", "species.empty", "Species cannot be empty");
        }

        if (pet.getBreed() == null || pet.getBreed().trim().isEmpty()) {
            errors.rejectValue("breed", "breed.empty", "Breed cannot be empty");
        }

        // Age check

        try{
            Integer.parseInt(pet.getAge());
        }catch (Exception e){
            errors.rejectValue("age", "age.invalid", "Age must be a number");
        }
        if (pet.getAge() == null) {
            errors.rejectValue("age", "age.null", "Valid age is required");
        } else if (Integer.parseInt(pet.getAge()) < 0) {
            errors.rejectValue("age", "age.negative", "Age cannot be negative");
        } else if (Integer.parseInt(pet.getAge()) > 150) {
            errors.rejectValue("age", "age.unrealistic", "Age is too high for a pet");
        }
    }
}
