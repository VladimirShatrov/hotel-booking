package t1intership.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1intership.userservice.dto.DepartmentData;
import t1intership.userservice.mapper.DepartmentMapper;
import t1intership.userservice.port.in.DepartmentInPort;
import t1intership.userservice.repository.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentInPort {

    private final DepartmentMapper mapper;
    private final DepartmentRepository repository;

    @Override
    public List<DepartmentData> getAllDepartment() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .toList();
    }
}
