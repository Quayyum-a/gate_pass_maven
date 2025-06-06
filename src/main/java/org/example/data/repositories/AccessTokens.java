package org.example.data.repositories;

import org.example.data.models.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AccessTokens extends MongoRepository<AccessToken, String> {
    AccessToken findByToken(String token);
    List<AccessToken> findByResidentIdAndStatus(String residentId, String status);
    List<AccessToken> findByResidentId(String residentId);
    @Query("{'$or': [{'visitorName': {$regex: ?0, $options: 'i'}}, {'token': {$regex: ?0, $options: 'i'}}], 'resident.id': ?1}")
    List<AccessToken> findBySearchTermAndResidentId(String search, String residentId);
}