package com.alenedaj.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "verification_codes")
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    @Id
    private String email;

    private String code;

    private LocalDateTime expiresAt;


}
