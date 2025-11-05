package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.RoomPresentation;
import org.relax.roomcatalogservice.domain.Hotel;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.domain.geo.HotelLocation;
import org.relax.roomcatalogservice.dto.CreateRoomRequest;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.mapper.RoomMapper;
import org.relax.roomcatalogservice.port.out.HotelRepository;
import org.relax.roomcatalogservice.port.out.RoomRepository;
import org.relax.roomcatalogservice.service.RoomService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomCatalogServiceTest {

    private RoomRepository roomRepository;
    private HotelRepository hotelRepository;
    private RoomMapper mapper;
    private RoomService service;

    @BeforeEach
    void setUp() {
        roomRepository = mock(RoomRepository.class);
        hotelRepository = mock(HotelRepository.class);
        mapper = mock(RoomMapper.class);
        service = new RoomService(roomRepository, hotelRepository, mapper);
    }

    @Test
    @DisplayName("createRoom: создаёт комнату и возвращает презентацию")
    void createRoom_ShouldSaveAndReturnPresentation() {
        var hotelId = UUID.randomUUID();
        var hotel = new Hotel(hotelId, "Hotel", new HotelLocation(1.0, 1.0));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        var req = new CreateRoomRequest("101", 2, hotelId.toString(), Set.of("Wi-Fi"));
        var room = Room.builder().id(UUID.randomUUID()).number("101").capacity(2).hotel(hotel).amenities(Set.of("Wi-Fi")).build();
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        var expectedPresentation = new RoomPresentation(room.getId(), "101", 2, null, Set.of("Wi-Fi"));
        when(mapper.entityToPresentation(any(Room.class))).thenReturn(expectedPresentation);

        var result = service.createRoom(req);

        verify(roomRepository).save(any(Room.class));
        assertThat(result.number()).isEqualTo("101");
        assertThat(result.capacity()).isEqualTo(2);
    }

    @Test
    @DisplayName("getRoomById: возвращает найденную комнату")
    void getRoomById_ShouldReturnPresentation() {
        var id = UUID.randomUUID();
        var hotel = new Hotel(UUID.randomUUID(), "H", new HotelLocation(1.0, 1.0));
        var room = Room.builder().id(id).number("101").capacity(2).hotel(hotel).build();

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        var expected = new RoomPresentation(id, "101", 2, null, Set.of());
        when(mapper.entityToPresentation(room)).thenReturn(expected);

        var result = service.getRoomById(id);

        assertThat(result.number()).isEqualTo("101");
        verify(roomRepository).findById(id);
    }

    @Test
    @DisplayName("getRoomById: бросает исключение, если не найдено")
    void getRoomById_ShouldThrowIfNotFound() {
        var id = UUID.randomUUID();
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRoomById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Room not found");
    }

    @Test
    @DisplayName("getAllRooms: возвращает все комнаты")
    void getAllRooms_ShouldReturnList() {
        var room = Room.builder().id(UUID.randomUUID()).number("101").capacity(2).build();
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(mapper.entityToPresentation(any(Room.class)))
                .thenReturn(new RoomPresentation(room.getId(), "101", 2, null, Set.of()));

        var result = service.getAllRooms();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).number()).isEqualTo("101");
    }

    @Test
    @DisplayName("updateRoom: обновляет поля комнаты")
    void updateRoom_ShouldUpdate() {
        var id = UUID.randomUUID();
        var hotel = new Hotel(UUID.randomUUID(), "Hotel", new HotelLocation(1.0, 1.0));
        var room = Room.builder().id(id).number("101").capacity(2).hotel(hotel).build();

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel)); // ✅ ВАЖНО
        when(roomRepository.save(room)).thenReturn(room);
        when(mapper.entityToPresentation(room))
                .thenReturn(new RoomPresentation(id, "102", 3, null, Set.of("TV")));

        var req = new UpdateRoomRequest("102", 3, hotel.getId().toString(), Set.of("TV"));

        var result = service.updateRoom(id, req);

        verify(roomRepository).save(room);
        assertThat(result.number()).isEqualTo("102");
        assertThat(result.capacity()).isEqualTo(3);
    }

    @Test
    @DisplayName("deleteRoom: удаляет комнату")
    void deleteRoom_ShouldDelete() {
        var id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(true);

        service.deleteRoom(id);

        verify(roomRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteRoom: бросает исключение, если комната не существует")
    void deleteRoom_ShouldThrowIfNotExists() {
        var id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteRoom(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Room not found");
    }

    @Test
    @DisplayName("existsById: возвращает true, если комната есть")
    void existsById_ShouldReturnTrue() {
        var id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(true);

        assertThat(service.existsById(id)).isTrue();
    }
}
