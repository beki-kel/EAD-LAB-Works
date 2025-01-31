package com.alenedaj.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "gas_stations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GasStation {
    @Id
    @Field("_id")
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private String location;
    private double latitude;
    private double longitude;
    private boolean fuelAvailable;
    private String trafficLevel = "UNDEFINED";

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isFuelAvailable() {
        return fuelAvailable;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setFuelAvailable(boolean fuelAvailable) {
        this.fuelAvailable = fuelAvailable;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
