package com.alenedaj.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "traffic_updates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrafficUpdate {
    @Id
    private String id;
    private String gasStationId;
    private String trafficStatus;
    private LocalDateTime timestamp;
}
