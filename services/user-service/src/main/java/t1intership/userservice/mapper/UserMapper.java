package t1intership.userservice.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import t1intership.userservice.domain.Department;
import t1intership.userservice.domain.User;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.repository.DepartmentRepository;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected DepartmentRepository departmentRepository;

    @Mapping(target = "departmentTitle", source = "department.title")
    public abstract UserData entityToDto(User user);

    @Mapping(target = "department", expression = "java(findDepartmentByTitle(data.departmentTitle()))")
    public abstract User dtoToEntity(UserData data);

    protected Department findDepartmentByTitle(String title) {
        if (title == null) {
            return null;
        }
        return departmentRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с названием '" + title + "' не найден"));
    }
}
