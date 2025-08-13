package t1intership.userservice.mapper;

import org.mapstruct.Mapper;
import t1intership.userservice.domain.Department;
import t1intership.userservice.dto.DepartmentData;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentData entityToDto(Department department);
}
