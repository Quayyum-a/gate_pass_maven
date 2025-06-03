package org.example.data.repositories;

import org.example.data.models.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessTokens extends MongoRepository<AccessToken, String> {

}
