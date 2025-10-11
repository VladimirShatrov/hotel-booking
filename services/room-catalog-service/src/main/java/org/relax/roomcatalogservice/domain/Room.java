package org.relax.roomcatalogservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Integer capacity;

    @Column
    private String location;

    public static RoomBuilder builder() { return new RoomBuilder(); }

    public static class RoomBuilder {
        private UUID id;
        private String name;
        private Integer capacity;
        private String location;

        RoomBuilder() {}

        public RoomBuilder id(UUID id) { this.id = id; return this; }
        public RoomBuilder name(String name) { this.name = name; return this; }
        public RoomBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }
        public RoomBuilder location(String location) { this.location = location; return this; }

        public Room build() { return new Room(id, name, capacity, location); }
    }
}
