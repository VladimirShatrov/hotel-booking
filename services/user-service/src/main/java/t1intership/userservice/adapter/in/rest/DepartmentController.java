package t1intership.userservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1intership.userservice.dto.DepartmentData;
import t1intership.userservice.port.in.DepartmentInPort;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department/v1")
public class DepartmentController {

    private final DepartmentInPort inPort;
    private static final String MEDIA_TYPE = "application/vnd.t1intership.department.v1+json";


    @GetMapping
    public ResponseEntity<List<DepartmentData>> getAllDepartments() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(inPort.getAllDepartment());
    }
}
