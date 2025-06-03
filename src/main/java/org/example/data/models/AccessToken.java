package org.example.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "access_tokens")
public class AccessToken {
    @Id
    private String id;
    private String token;
    private LocalDateTime creationDate;
    private LocalDateTime expiryDate;
    private boolean isUsed;
    @DBRef
    private Resident resident;
    @DBRef
    private Visitor visitor;
    private String visitorPhoneNumber;
    private String whomToSee;
    private String visitorName;


}
