package t1internship.placesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import t1internship.placesservice.api.controller.GlobalExceptionHandler;
import t1internship.placesservice.api.controller.SpaceController;
import t1internship.placesservice.api.dto.request.WorkSpaceRequest;
import t1internship.placesservice.api.dto.response.SpaceResponse;
import t1internship.placesservice.api.dto.response.WorkSpaceResponse;
import t1internship.placesservice.api.exceptions.NotFoundSpaceException;
import t1internship.placesservice.api.service.SpaceService;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SpaceControllerTest {

    @Mock
    private SpaceService spaceService;

    @InjectMocks
    private SpaceController spaceController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(spaceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createSpaceInFloorById_success() throws Exception {
        WorkSpaceRequest request = new WorkSpaceRequest("Workspace 1", 10, 1L, true, 4, 2);
        WorkSpaceResponse response = new WorkSpaceResponse();
        response.setId(1L);
        response.setName("Workspace 1");
        response.setSeatingCapacity(10);
        response.setFloorId(1L);
        response.setSpaceType("WORKSPACE");
        response.setHasComputer(true);
        response.setNumberOfSockets(4);
        response.setNumberOfMonitors(2);

        when(spaceService.createSpace(any(WorkSpaceRequest.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/v1/spaces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Workspace 1"));

        verify(spaceService).createSpace(any(WorkSpaceRequest.class), eq(1L));
    }

    @Test
    void createSpacesInFloorById_success() throws Exception {
        List<WorkSpaceRequest> requests = List.of(
                new WorkSpaceRequest("Workspace 1", 10, 1L, true, 4, 2),
                new WorkSpaceRequest("Workspace 2", 8, 1L, false, 2, 1)
        );
        List<WorkSpaceResponse> responses = List.of(
                new WorkSpaceResponse(1L, "Workspace 1", 10, 1L, true, 4, 2),
                new WorkSpaceResponse(2L, "Workspace 2", 8, 1L, false, 2, 1)
        );



        mockMvc.perform(post("/api/v1/spaces/many/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated());

        verify(spaceService).createSpaces(anyList(), eq(1L));
    }

    @Test
    void getSpacesByFloorId_success() throws Exception {
        List<WorkSpaceResponse> responses = List.of(
                new WorkSpaceResponse(1L, "Workspace 1", 10, 1L, true, 4, 2)
        );

        mockMvc.perform(get("/api/v1/spaces/1"))
                .andExpect(status().isOk());

        verify(spaceService).getSpacesByFloorId(1L);
    }

    @Test
    void updateSpace_success() throws Exception {
        WorkSpaceRequest request = new WorkSpaceRequest("Updated Workspace", 12, 1L, true, 6, 3);
        WorkSpaceResponse response = new WorkSpaceResponse(1L, "Updated Workspace", 12, 1L, true, 6, 3);

        when(spaceService.updateSpace(eq(1L), any(WorkSpaceRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/spaces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Workspace"));

        verify(spaceService).updateSpace(eq(1L), any(WorkSpaceRequest.class));
    }

    @Test
    void updateSpace_notFound() throws Exception {
        WorkSpaceRequest request = new WorkSpaceRequest("Updated Workspace", 12, 1L, true, 6, 3);

        when(spaceService.updateSpace(eq(1L), any(WorkSpaceRequest.class)))
                .thenThrow(new NotFoundSpaceException("Not found Space with id: 1"));

        mockMvc.perform(patch("/api/v1/spaces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found Space with id: 1"));

        verify(spaceService).updateSpace(eq(1L), any(WorkSpaceRequest.class));
    }

    @Test
    void deleteSpace_success() throws Exception {
        doNothing().when(spaceService).deleteSpace(1L);

        mockMvc.perform(delete("/api/v1/spaces/1"))
                .andExpect(status().isNoContent());

        verify(spaceService).deleteSpace(1L);
    }
}