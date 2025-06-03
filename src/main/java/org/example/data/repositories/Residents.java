package org.example.data.repositories;

import org.example.data.models.Resident;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Residents extends MongoRepository<Resident, String> {

    Resident findByEmail(String email);
}
