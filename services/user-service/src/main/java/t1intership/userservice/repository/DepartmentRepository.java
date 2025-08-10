package t1intership.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t1intership.userservice.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
