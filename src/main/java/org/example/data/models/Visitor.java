package org.example.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "visitors")
public class Visitor {
    @Id
    private String id;
    private String fullName;
    private String phoneNumber;
    @DBRef
    private Resident whomToSee;


}