package t1internship.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import t1internship.orderservice.data.entity.Order;
import t1internship.orderservice.data.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT b FROM Order b WHERE b.spaceId = :spaceId " +
            "AND b.status = :status " +
            "AND ((b.startTime <= :endTime AND b.endTime >= :startTime))")
    List<Order> findOrdersBySpaceIdAndTimeRangeAndStatus(
            @Param("spaceId") Long spaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") OrderStatus status
    );

    @Query("SELECT o FROM Order o WHERE o.status = 'CONFIRMED' AND o.endTime < :currentTime")
    List<Order> findConfirmedOrdersWithExpiredEndTime(@Param("currentTime") LocalDateTime currentTime);

    List<Order> findOrdersByUserId(UUID userId);

    List<Order> findByStatusIn(List<String> statuses);
}
