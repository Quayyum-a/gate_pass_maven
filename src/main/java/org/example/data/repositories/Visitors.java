package org.example.data.repositories;

import org.example.data.models.Visitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface Visitors extends MongoRepository<Visitor, String> {
    @Query("{'whomToSee.id': ?0, 'checkIn': {$gte: ?1, $lte: ?2}}")
    List<Visitor> findByResidentIdAndDateRange(String residentId, LocalDateTime fromDate, LocalDateTime toDate);
    @Query("{'$or': [{'fullName': {$regex: ?0, $options: 'i'}}, {'phoneNumber': {$regex: ?0, $options: 'i'}}]}")
    List<Visitor> findBySearchTerm(String search);
}