package org.relax.room.availability.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_availability")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Room roomId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
