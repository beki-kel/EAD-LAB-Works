package com.alenedaj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTrafficDTO {
    @NotBlank(message = "Traffic level is required")
    private String trafficLevel;
}
