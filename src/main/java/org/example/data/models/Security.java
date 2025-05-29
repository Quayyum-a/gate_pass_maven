package org.example.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "securities")
public class Security {
    @Id
    private String id;
    private String name;
    private String employeeId;
}