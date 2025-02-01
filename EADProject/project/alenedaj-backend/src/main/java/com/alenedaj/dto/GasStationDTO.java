package com.alenedaj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class GasStationDTO {

    private ObjectId _id;
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")  // Location name (Admins input this)
    private String location;

    private Double latitude;  // Auto-filled
    private Double longitude; // Auto-filled

    private boolean fuelAvailable;
    private String trafficLevel;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isFuelAvailable() {
        return fuelAvailable;
    }

    public void setFuelAvailable(boolean fuelAvailable) {
        this.fuelAvailable = fuelAvailable;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }// LOW, MEDIUM, HIGH

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }
}
