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
import t1internship.placesservice.api.controller.FloorController;
import t1internship.placesservice.api.controller.GlobalExceptionHandler;
import t1internship.placesservice.api.dto.request.FloorRequest;
import t1internship.placesservice.api.dto.response.FloorResponse;
import t1internship.placesservice.api.exceptions.NotFoundFloorException;
import t1internship.placesservice.api.service.FloorService;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FloorControllerTest {

    @Mock
    private FloorService floorService;

    @InjectMocks
    private FloorController floorController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(floorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createFloorInLocationById_success() throws Exception {
        FloorRequest request = new FloorRequest(2, 1L, List.of());
        FloorResponse response = new FloorResponse(2L, 2, 1L, List.of());

        when(floorService.createFloor(any(FloorRequest.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/v1/floors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.floorNumber").value(2));

        verify(floorService).createFloor(any(FloorRequest.class), eq(1L));
    }

    @Test
    void createFloorsInLocationById_success() throws Exception {
        List<FloorRequest> requests = List.of(
                new FloorRequest(3, 1L, List.of()),
                new FloorRequest(4, 1L, List.of())
        );
        List<FloorResponse> responses = List.of(
                new FloorResponse(3L, 3, 1L, List.of()),
                new FloorResponse(4L, 4, 1L, List.of())
        );

        when(floorService.createFloors(eq(1L), anyList())).thenReturn(responses);

        mockMvc.perform(post("/api/v1/floors/many/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[1].id").value(4));

        verify(floorService).createFloors(eq(1L), anyList());
    }

    @Test
    void getFloorsByLocationId_success() throws Exception {
        List<FloorResponse> responses = List.of(new FloorResponse(1L, 1, 1L, List.of()));

        when(floorService.getFloorsByLocationId(1L, true)).thenReturn(responses);

        mockMvc.perform(get("/api/v1/floors/1")
                        .param("includeSpaces", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(floorService).getFloorsByLocationId(1L, true);
    }

    @Test
    void updateFloor_success() throws Exception {
        FloorRequest request = new FloorRequest(5, 1L, List.of());
        FloorResponse response = new FloorResponse(1L, 5, 1L, List.of());

        when(floorService.updateFloor(eq(1L), any(FloorRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/floors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.floorNumber").value(5));

        verify(floorService).updateFloor(eq(1L), any(FloorRequest.class));
    }

    @Test
    void updateFloor_notFound() throws Exception {
        FloorRequest request = new FloorRequest(5, 1L, List.of());

        when(floorService.updateFloor(eq(1L), any(FloorRequest.class)))
                .thenThrow(new NotFoundFloorException("Not found Floor with id: 1"));

        mockMvc.perform(patch("/api/v1/floors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found Floor with id: 1"));

        verify(floorService).updateFloor(eq(1L), any(FloorRequest.class));
    }

    @Test
    void deleteFloor_success() throws Exception {
        doNothing().when(floorService).deleteFloor(1L);

        mockMvc.perform(delete("/api/v1/floors/1"))
                .andExpect(status().isNoContent());

        verify(floorService).deleteFloor(1L);
    }
}
