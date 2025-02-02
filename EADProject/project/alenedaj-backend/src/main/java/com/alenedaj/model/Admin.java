package com.alenedaj.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "admins")
@NoArgsConstructor @AllArgsConstructor
public class Admin {
    @Id
    private String id;
    private String username;
    private String password;
    private String gasStationId;
}
