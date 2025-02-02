package com.alenedaj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasStationDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")  // Location name (Admins input this)
    private String location;

    private Double latitude;  // Auto-filled
    private Double longitude; // Auto-filled

    private boolean fuelAvailable;
    private String trafficLevel;

}
