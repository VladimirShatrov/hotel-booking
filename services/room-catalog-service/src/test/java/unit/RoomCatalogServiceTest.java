package unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.dto.RoomData;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.mapper.RoomMapper;
import org.relax.roomcatalogservice.port.out.RoomRepository;
import org.relax.roomcatalogservice.service.RoomService;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomCatalogServiceTest {

    @Mock
    private RoomRepository repository;

    @Mock
    private RoomMapper mapper;

    @InjectMocks
    private RoomService service;

    @Test
    public void getRoomById_Found_ReturnsDto() {
        UUID id = UUID.randomUUID();
        Room room = new Room(id, "Room1", 10, "1st floor");
        RoomData dto = new RoomData(id, "Room1", 10, "1st floor");

        when(repository.findById(id)).thenReturn(Optional.of(room));
        when(mapper.entityToDto(room)).thenReturn(dto);

        RoomData result = service.getRoomById(id);
        assertEquals(dto, result);

        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).entityToDto(room);
    }

    @Test
    public void getRoomById_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getRoomById(id));
    }

    @Test
    public void updateRoom_PartialUpdate_UpdatesOnlyProvidedFields() {
        UUID id = UUID.randomUUID();
        Room room = new Room(id, "Room1", 10, "1st floor");
        Room saved = new Room(id, "UpdatedRoom", 10, "1st floor");
        UpdateRoomRequest request = new UpdateRoomRequest("UpdatedRoom", null, null);
        RoomData dto = new RoomData(id, "UpdatedRoom", 10, "1st floor");

        when(repository.findById(id)).thenReturn(Optional.of(room));
        when(repository.save(room)).thenReturn(saved);
        when(mapper.entityToDto(saved)).thenReturn(dto);

        RoomData result = service.updateRoom(id, request);
        assertEquals("UpdatedRoom", result.name());
        assertEquals(10, result.capacity());

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(room);
        verify(mapper, times(1)).entityToDto(saved);
    }
}