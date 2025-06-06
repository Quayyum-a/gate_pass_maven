package org.example.data.repositories;

import org.example.data.models.Security;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Securities extends MongoRepository<Security, String> {
    Security findByEmail(String email);
}
