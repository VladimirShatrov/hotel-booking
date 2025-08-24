package t1internship.orderservice.data.repository;

import t1internship.orderservice.data.entity.Order;
import t1internship.orderservice.data.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderCustomRepository {
    List<Order> findOrdersByCriteria(Long floorId, Long spaceId, LocalDateTime startTime,
                                     LocalDateTime endTime, List<OrderStatus> statuses);
}
