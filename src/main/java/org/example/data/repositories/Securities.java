package org.example.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface Securities extends MongoRepository<Securities, String> {
}
