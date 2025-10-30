package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.AdoptionListItemDTO;
import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdoptionMapperTest {

    private final AdoptionMapper mapper = AdoptionMapper.INSTANCE;

    @Test
    void toModelFromAddRequest_shouldMapFieldsAndSetDefaults() {
        // given
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");

        User publisher = new User();
        publisher.setId(1L);

        List<String> images = List.of("img1.png", "img2.png");
        String contactNumber = "123456789";

        AdoptionAddRequestDTO dto = new AdoptionAddRequestDTO(
                pet,
                publisher.getId(),
                images,
                contactNumber
        );

        // when
        AdoptionEntry entry = mapper.toModelFromAddRequest(dto);

        // then
        assertNotNull(entry);
        assertEquals(pet, entry.getPet());
        assertNull(entry.getPublisher());
        assertEquals(images, entry.getAdditionalImages());
        assertEquals(contactNumber, entry.getContactNumber());

        // defaults from @AfterMapping
        assertNotNull(entry.getCreatedAt());
        assertTrue(entry.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertNull(entry.getAdoptedAt());
        assertNull(entry.getAdopter());
        assertNull(entry.getId()); // ignored in mapping
    }

    @Test
    void toListItemFromModel_shouldMapFields() {
        // given
        Pet pet = new Pet();
        pet.setId(2);
        pet.setName("Luna");

        User publisher = new User();
        publisher.setUsername("alice");

        List<String> images = List.of("extra1.png");
        String contactNumber = "987654321";

        AdoptionEntry entry = AdoptionEntry.builder()
                .id(100L)
                .pet(pet)
                .publisher(publisher)
                .additionalImages(images)
                .contactNumber(contactNumber)
                .build();

        // when
        AdoptionListItemDTO dto = mapper.toListItemFromModel(entry);

        // then
        assertNotNull(dto);

        assertEquals(100L, dto.id());
        assertNotNull(dto.pet());
        assertInstanceOf(PetDTO.class, dto.pet()); // mapped via PetMapper
        assertEquals("alice", dto.publisher().username());
        assertEquals(images, dto.additionalImages());
        assertEquals(contactNumber, dto.contactNumber());

    }

    @Test
    void toListItemsFromModels_shouldMapList() {
        // given
        Pet pet = new Pet();
        pet.setId(2);
        pet.setName("Luna");

        User publisher = new User();
        publisher.setUsername("alice");

        List<String> images = List.of("extra1.png");
        String contactNumber = "987654321";

        AdoptionEntry entry = AdoptionEntry.builder()
                .id(100L)
                .pet(pet)
                .publisher(publisher)
                .additionalImages(images)
                .contactNumber(contactNumber)
                .build();

        // when
        List<AdoptionListItemDTO> dtos = mapper.toListItemsFromModels(List.of(entry));

        // then
        assertNotNull(dtos);
        assertEquals(1, dtos.size());

        AdoptionListItemDTO dto = dtos.getFirst();
        assertEquals(100L, dto.id());
        assertNotNull(dto.pet());
        assertInstanceOf(PetDTO.class, dto.pet()); // mapped via PetMapper
        assertEquals("alice", dto.publisher().username());
        assertEquals(images, dto.additionalImages());
        assertEquals(contactNumber, dto.contactNumber());
    }
}
