package t1internship.placesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import t1internship.placesservice.api.controller.GlobalExceptionHandler;
import t1internship.placesservice.api.controller.LocationController;
import t1internship.placesservice.api.dto.request.LocationRequest;
import t1internship.placesservice.api.dto.response.LocationResponse;
import t1internship.placesservice.api.exceptions.NotFoundLocationException;
import t1internship.placesservice.api.service.LocationService;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createLocation_success() throws Exception {
        LocationRequest request = new LocationRequest("Main Office", "123 Main St", "4A", "12345", "+1234567890", "office@example.com", "New York", List.of());
        LocationResponse response = new LocationResponse();
        response.setId(1L);
        response.setName("Main Office");

        when(locationService.createLocation(any(LocationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Main Office"));

        verify(locationService).createLocation(any(LocationRequest.class));
    }

    @Test
    void createLocations_success() throws Exception {
        List<LocationRequest> requests = List.of(
                new LocationRequest("Main Office", "123 Main St", "4A", "12345", "+1234567890", "office1@example.com", "New York", List.of()),
                new LocationRequest("Branch Office", "456 Elm St", "5B", "67890", "+0987654321", "office2@example.com", "Boston", List.of())
        );
        List<LocationResponse> responses = List.of(
                new LocationResponse(1L, "Main Office", "123 Main St", "4A", "12345", "+1234567890", "office1@example.com", "New York", List.of()),
                new LocationResponse(2L, "Branch Office", "456 Elm St", "5B", "67890", "+0987654321", "office2@example.com", "Boston", List.of())
        );

        when(locationService.createLocations(anyList())).thenReturn(responses);

        mockMvc.perform(post("/api/v1/locations/many")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(locationService).createLocations(anyList());
    }

    @Test
    void getAllLocationsWithFloorsAndSpaces_success() throws Exception {
        List<LocationResponse> responses = List.of(new LocationResponse(1L, "Main Office", "123 Main St", "4A", "12345", "+1234567890", "office@example.com", "New York", List.of()));

        when(locationService.getLocations(true, true)).thenReturn(responses);

        mockMvc.perform(get("/api/v1/locations")
                        .param("includeFloors", "true")
                        .param("includeSpaces", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(locationService).getLocations(true, true);
    }

    @Test
    void getLocationById_success() throws Exception {
        LocationResponse response = new LocationResponse(1L, "Main Office", "123 Main St", "4A", "12345", "+1234567890", "office@example.com", "New York", List.of());

        when(locationService.getLocationById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(locationService).getLocationById(1L);
    }

    @Test
    void getLocationById_notFound() throws Exception {
        when(locationService.getLocationById(1L)).thenThrow(new NotFoundLocationException("Not found Location with id: 1"));

        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found Location with id: 1"));

        verify(locationService).getLocationById(1L);
    }

    @Test
    void updateLocation_success() throws Exception {
        LocationRequest request = new LocationRequest("Updated Office", "123 New St", "4B", "54321", "+1234567890", "updated@example.com", "New York", List.of());
        LocationResponse response = new LocationResponse(1L, "Updated Office", "123 New St", "4B", "54321", "+1234567890", "updated@example.com", "New York", List.of());

        when(locationService.updateLocation(eq(1L), any(LocationRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Office"));

        verify(locationService).updateLocation(eq(1L), any(LocationRequest.class));
    }

    @Test
    void deleteLocation_success() throws Exception {
        doNothing().when(locationService).deleteLocation(1L);

        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isNoContent());

        verify(locationService).deleteLocation(1L);
    }
}