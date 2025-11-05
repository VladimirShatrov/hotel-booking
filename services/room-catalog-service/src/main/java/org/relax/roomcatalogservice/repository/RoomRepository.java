package org.relax.roomcatalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.relax.roomcatalogservice.domain.Room;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    boolean existsById(UUID id);
}
