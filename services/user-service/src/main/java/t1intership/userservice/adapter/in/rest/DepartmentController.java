package t1intership.userservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import t1intership.userservice.dto.DepartmentData;
import t1intership.userservice.port.in.DepartmentInPort;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentInPort inPort;
    private static final String MEDIA_TYPE = "application/vnd.t1internship.department.v1+json";


    @GetMapping(produces = MEDIA_TYPE)
    public ResponseEntity<List<DepartmentData>> getAllDepartments() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(inPort.getAllDepartment());
    }

    @PostMapping(produces = MEDIA_TYPE)
    public ResponseEntity<DepartmentData> createDepartment(
            @RequestBody String departmentName,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        DepartmentData savedDepartment = inPort.save(departmentName);

        return ResponseEntity.created(
                    uriComponentsBuilder
                            .path("api/v1/department/{id}")
                            .build(Map.of("id", savedDepartment.id()))
                )
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(savedDepartment);
    }

    @GetMapping(
            value = "/{id}",
            produces = MEDIA_TYPE
    )
    public ResponseEntity<DepartmentData> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(inPort.getById(id));
    }
}
