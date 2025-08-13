package t1intership.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfileRequest(
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("departmentId") Long departmentId,
        @JsonProperty("jobTitle") String jobTitle
) {}
