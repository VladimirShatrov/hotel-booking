package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation;
import org.relax.roomcatalogservice.domain.Hotel;
import org.relax.roomcatalogservice.domain.geo.HotelLocation;
import org.relax.roomcatalogservice.dto.CreateHotelRequest;
import org.relax.roomcatalogservice.dto.UpdateHotelRequest;
import org.relax.roomcatalogservice.port.out.HotelRepository;
import org.relax.roomcatalogservice.service.HotelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    private HotelRepository repository;
    private HotelService service;

    @BeforeEach
    void setUp() {
        repository = mock(HotelRepository.class);
        service = new HotelService(repository);
    }

    @Test
    @DisplayName("createHotel: сохраняет и возвращает презентацию")
    void createHotel_ShouldSaveAndReturnPresentation() {
        var request = new CreateHotelRequest("Hilton", 55.75, 37.61);

        var result = service.createHotel(request);

        verify(repository).save(any(Hotel.class));
        assertThat(result.name()).isEqualTo("Hilton");
        assertThat(result.latitude()).isEqualTo(55.75);
        assertThat(result.longitude()).isEqualTo(37.61);
    }

    @Test
    @DisplayName("getHotelById: возвращает найденный отель")
    void getHotelById_ShouldReturnPresentation() {
        var id = UUID.randomUUID();
        var hotel = new Hotel(id, "Radisson", new HotelLocation(59.93, 30.31));
        when(repository.findById(id)).thenReturn(Optional.of(hotel));

        var result = service.getHotelById(id);

        assertThat(result.name()).isEqualTo("Radisson");
        assertThat(result.latitude()).isEqualTo(59.93);
        assertThat(result.longitude()).isEqualTo(30.31);
    }

    @Test
    @DisplayName("getHotelById: бросает исключение, если не найден")
    void getHotelById_ShouldThrowIfNotFound() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getHotelById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hotel not found");
    }

    @Test
    @DisplayName("getAllHotels: возвращает все отели")
    void getAllHotels_ShouldReturnList() {
        var h1 = new Hotel(UUID.randomUUID(), "A", new HotelLocation(10.0, 20.0));
        var h2 = new Hotel(UUID.randomUUID(), "B", new HotelLocation(30.0, 40.0));
        when(repository.findAll()).thenReturn(List.of(h1, h2));

        var result = service.getAllHotels();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("A");
        assertThat(result.get(1).name()).isEqualTo("B");
    }

    @Test
    @DisplayName("updateHotel: обновляет поля отеля")
    void updateHotel_ShouldUpdateHotel() {
        var id = UUID.randomUUID();
        var hotel = new Hotel(id, "Old", new HotelLocation(1.0, 1.0));
        when(repository.findById(id)).thenReturn(Optional.of(hotel));

        var update = new UpdateHotelRequest("New", 2.0, 3.0);

        var result = service.updateHotel(id, update);

        verify(repository).save(hotel);
        assertThat(hotel.getName()).isEqualTo("New");
        assertThat(hotel.getLocation().latitude()).isEqualTo(2.0);
        assertThat(result.name()).isEqualTo("New");
    }

    @Test
    @DisplayName("updateHotel: бросает исключение, если отель не найден")
    void updateHotel_ShouldThrowIfNotFound() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        var req = new UpdateHotelRequest("Any", 1.0, 1.0);

        assertThatThrownBy(() -> service.updateHotel(id, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hotel not found");
    }

    @Test
    @DisplayName("deleteHotel: удаляет существующий отель")
    void deleteHotel_ShouldDelete() {
        var id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.deleteHotel(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("deleteHotel: бросает исключение, если отель не существует")
    void deleteHotel_ShouldThrowIfNotExists() {
        var id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteHotel(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hotel not found");
    }
}
