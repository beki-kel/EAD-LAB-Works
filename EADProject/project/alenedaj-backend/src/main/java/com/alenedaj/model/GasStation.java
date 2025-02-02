package com.alenedaj.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "gas_stations")
@NoArgsConstructor @AllArgsConstructor
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


}
