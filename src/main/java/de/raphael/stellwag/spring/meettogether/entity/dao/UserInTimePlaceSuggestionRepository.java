package de.raphael.stellwag.spring.meettogether.entity.dao;

import de.raphael.stellwag.spring.meettogether.entity.model.UserInTimePlaceSuggestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserInTimePlaceSuggestionRepository extends MongoRepository<UserInTimePlaceSuggestionEntity, String> {
    Optional<UserInTimePlaceSuggestionEntity> findByUserIdAndTimePlaceSuggestionId(String userId, String timePlaceId);

    List<UserInTimePlaceSuggestionEntity> findByTimePlaceSuggestionId(String timePlaceId);
}
