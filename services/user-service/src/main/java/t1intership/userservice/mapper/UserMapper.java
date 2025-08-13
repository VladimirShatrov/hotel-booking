package t1intership.userservice.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import t1intership.userservice.domain.Department;
import t1intership.userservice.domain.User;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.repository.DepartmentRepository;

@Mapper(componentModel = "spring", uses = {DepartmentRepository.class})
public interface UserMapper {

    @Mapping(target = "departmentTitle", source = "department.title")
    UserData entityToDto(User user);

    @Mapping(target = "department", expression = "java(findDepartmentByTitle(data.departmentTitle()))")
    User dtoToEntity(UserData data);

    default Department findDepartmentByTitle(String title, @Context DepartmentRepository departmentRepository) {
        if (title == null) {
            return null;
        }
        return departmentRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с названием '" + title + "' не найден"));
    }
}
