package t1internship.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1internship.orderservice.data.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
}
