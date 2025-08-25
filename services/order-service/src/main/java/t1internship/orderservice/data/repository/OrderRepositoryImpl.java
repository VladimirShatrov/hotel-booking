package t1internship.orderservice.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import t1internship.orderservice.data.entity.Order;
import t1internship.orderservice.data.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderCustomRepository{

    private final EntityManager entityManager;
    @Override
    public List<Order> findOrdersByCriteria(Long floorId, Long spaceId, LocalDateTime startTime, LocalDateTime endTime, List<OrderStatus> statuses) {
        CriteriaBuilder cb  = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> order = cq.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        if(floorId != null) {
            predicates.add(cb.equal(order.get("floorId"), floorId));
        }
        if (spaceId != null) {
            predicates.add(cb.equal(order.get("spaceId"), spaceId));
        }
        if (startTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(order.get("startTime"), startTime));
        }
        if (endTime != null) {
            predicates.add(cb.lessThanOrEqualTo(order.get("endTime"), endTime));
        }
        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(order.get("status").in(statuses));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(order.get("createdAt")));
        TypedQuery<Order> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
