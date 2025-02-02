package com.alenedaj.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
@NoArgsConstructor
public class User {
    @Id
    @Field("_id")
    private ObjectId id;
    private String name;
    @NotNull
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    private Set<String> roles;

    private String location;
    private Double latitude;
    private Double longitude;

}
