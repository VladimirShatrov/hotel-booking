package t1intership.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(
        @JsonProperty("firstName")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "Имя может содержать только буквы, пробелы и дефисы")
        String firstName,

        @JsonProperty("lastName")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "Фамилия может содержать только буквы, пробелы и дефисы")
        String lastName,
        @JsonProperty("departmentId") Long departmentId,
        @JsonProperty("jobTitle") String jobTitle
) {}
