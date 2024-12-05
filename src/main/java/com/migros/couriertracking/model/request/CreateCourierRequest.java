package com.migros.couriertracking.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateCourierRequest {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Identity number cannot be empty")
    @Pattern(regexp = "^[0-9]{11}$", message = "Identity number must be 11 digits")
    private String identityNumber;
} 