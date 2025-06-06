package org.example.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "visitors")
public class Visitor {
    @Id
    private String id;
    private String fullName;
    private String phoneNumber;
    private String purpose;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    @DBRef
    private Resident whomToSee;
}