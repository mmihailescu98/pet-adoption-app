package cloudflight.integra.backend.service.validators;

public class ValidatorException extends RuntimeException {
    public ValidatorException(String errorMessage) {
        super(errorMessage);
    }
}