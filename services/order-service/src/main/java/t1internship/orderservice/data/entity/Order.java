package t1internship.orderservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID userId;

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
    private OrderStatus status;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt ;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
