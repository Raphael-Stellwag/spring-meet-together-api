package de.raphael.stellwag.spring.meettogether.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimePlaceSuggestionRepository extends MongoRepository<TimePlaceSuggestionEntity, String> {
    List<TimePlaceSuggestionEntity> findByEventId(String eventId);
}
