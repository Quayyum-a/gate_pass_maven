package org.example.data.repositories;

import org.example.data.models.Visitor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Visitors extends MongoRepository<Visitor, String> {
}
