package cloudflight.integra.backend.listener;

public class PetAddedEvent {
    private final Integer petId;
    private final Long ownerUserId;

    public PetAddedEvent(Integer petId, Long ownerUserId) {
        this.petId = petId;
        this.ownerUserId = ownerUserId;
    }

    public Integer getPetId() {
        return petId;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }
}