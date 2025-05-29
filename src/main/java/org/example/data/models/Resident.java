package org.example.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "residents")
public class Resident {
    @Id
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;

}
