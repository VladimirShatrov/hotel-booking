package t1intership.userservice.port.in;

import t1intership.userservice.dto.DepartmentData;

import java.util.List;

public interface DepartmentInPort {
    List<DepartmentData> getAllDepartment();
}
