package t1internship.orderservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long spaceId;

    @Column(nullable = false)
    private Long floorId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt ;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
