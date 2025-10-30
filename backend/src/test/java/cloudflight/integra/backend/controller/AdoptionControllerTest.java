package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.AdoptionService;
import cloudflight.integra.backend.service.FavoritePetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdoptionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        cloudflight.integra.backend.security.JwtAuthenticationFilter.class,
                        cloudflight.integra.backend.security.SecurityConfig.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AdoptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdoptionService adoptionService;

    @MockitoBean
    private FavoritePetService favoritePetService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAdoptionEndpoint_getListedAdoptions() throws Exception {
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            // Mock authenticated user
            var mockUser = mock(CustomUserDetails.class);
            when(mockUser.getId()).thenReturn(1L);
            jwtUtilMock.when(JwtUtil::getAuthenticatedUser).thenReturn(mockUser);

            // Mock dependencies
            when(adoptionService.getPendingAdoptions()).thenReturn(mockAdoptionEntries());
            when(favoritePetService.getUserFavoritePetIds(1L)).thenReturn(Set.of());

            // Perform request
            mockMvc.perform(get("/api/adoptions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(5))
                    .andExpect(jsonPath("$[0].contactNumber").value("123-456-7890"));
        }
    }

    @Test
    void createAdoptionListing_success() throws Exception {
        var requestDto = buildAdoptionAddRequestDTO();
        when(adoptionService.createAdoption(any()))
                .thenReturn(AdoptionMapper.INSTANCE.toModelFromAddRequest(requestDto));

        mockMvc.perform(post("/api/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactNumber").value("123-456-7890"));
    }

    // --- Helpers ---

    private AdoptionAddRequestDTO buildAdoptionAddRequestDTO() {
        var pet = new Pet(
                1, "Dog", "Golden Retriever", "Buddy", "New York", "3",
                "Friendly and energetic family dog.", "https://example.com/images/dog1.jpg"
        );

        var publisher = User.builder()
                .id(1L)
                .username("alice")
                .password("password123")
                .name("Alice Johnson")
                .phone("123-456-7890")
                .email("alice@example.com")
                .location("New York")
                .imgURL("https://example.com/images/alice.jpg")
                .bio("Animal lover and long-time volunteer at the local shelter.")
                .roles(Set.of("ROLE_USER"))
                .build();

        return new AdoptionAddRequestDTO(
                pet,
                publisher.getId(),
                List.of("img1a.jpg", "img1b.jpg"),
                publisher.getPhone()
        );
    }

    private List<User> mockUsers() {
        return List.of(
                User.builder().id(1L).username("alice").password("password123").name("Alice Johnson")
                        .phone("123-456-7890").email("alice@example.com").location("New York")
                        .imgURL("https://example.com/images/alice.jpg").bio("Animal lover.").roles(Set.of("ROLE_USER")).build(),
                User.builder().id(2L).username("bob").password("securePass").name("Bob Smith")
                        .phone("234-567-8901").email("bob@example.com").location("Chicago")
                        .imgURL("https://example.com/images/bob.jpg").bio("Loves dogs.").roles(Set.of("ROLE_USER")).build(),
                User.builder().id(3L).username("charlie").password("qwerty").name("Charlie Brown")
                        .phone("345-678-9012").email("charlie@example.com").location("Boston")
                        .imgURL("https://example.com/images/charlie.jpg").bio("Cat owner.").roles(Set.of("ROLE_USER")).build(),
                User.builder().id(4L).username("diana").password("mypassword").name("Diana Prince")
                        .phone("456-789-0123").email("diana@example.com").location("Los Angeles")
                        .imgURL("https://example.com/images/diana.jpg").bio("Rescuer.").roles(Set.of("ROLE_USER")).build(),
                User.builder().id(5L).username("edward").password("letmein").name("Edward Johnson")
                        .phone("567-890-1234").email("edward@example.com").location("Seattle")
                        .imgURL("https://example.com/images/edward.jpg").bio("Moderator.").roles(Set.of("ROLE_USER")).build()
        );
    }

    private List<Pet> mockPets() {
        return List.of(
                new Pet(1, "Dog", "Golden Retriever", "Buddy", "New York", "3",
                        "Friendly and energetic family dog.", "https://example.com/images/dog1.jpg"),
                new Pet(2, "Cat", "Siamese", "Luna", "Los Angeles", "2",
                        "Affectionate cat who loves attention.", "https://example.com/images/cat1.jpg"),
                new Pet(3, "Dog", "German Shepherd", "Max", "Chicago", "4",
                        "Loyal and protective.", "https://example.com/images/dog2.jpg"),
                new Pet(4, "Rabbit", "Holland Lop", "Snowball", "San Francisco", "1",
                        "Cute and cuddly rabbit.", "https://example.com/images/rabbit1.jpg"),
                new Pet(5, "Dog", "Beagle", "Charlie", "Miami", "5",
                        "Playful beagle.", "https://example.com/images/dog3.jpg")
        );
    }

    private List<AdoptionEntry> mockAdoptionEntries() {
        var users = mockUsers();
        var pets = mockPets();

        return List.of(
                AdoptionEntry.builder().id(1L).pet(pets.get(0)).publisher(users.get(0))
                        .additionalImages(List.of("img1a.jpg", "img1b.jpg")).contactNumber("123-456-7890")
                        .adopter(null).createdAt(LocalDateTime.now().minusDays(10)).adoptedAt(null).build(),
                AdoptionEntry.builder().id(2L).pet(pets.get(1)).publisher(users.get(1))
                        .additionalImages(List.of("img2a.jpg")).contactNumber("987-654-3210")
                        .adopter(null).createdAt(LocalDateTime.now().minusDays(20)).adoptedAt(null).build(),
                AdoptionEntry.builder().id(3L).pet(pets.get(2)).publisher(users.get(2))
                        .additionalImages(List.of()).contactNumber("555-111-2222")
                        .adopter(null).createdAt(LocalDateTime.now().minusDays(15)).adoptedAt(null).build(),
                AdoptionEntry.builder().id(4L).pet(pets.get(3)).publisher(users.get(3))
                        .additionalImages(List.of("img4a.jpg", "img4b.jpg")).contactNumber("444-333-2222")
                        .adopter(null).createdAt(LocalDateTime.now().minusDays(2)).adoptedAt(null).build(),
                AdoptionEntry.builder().id(5L).pet(pets.get(4)).publisher(users.get(4))
                        .additionalImages(List.of("img5a.jpg")).contactNumber("111-222-3333")
                        .adopter(null).createdAt(LocalDateTime.now().minusDays(30)).adoptedAt(null).build()
        );
    }
}
