package de.raphael.stellwag.spring.meettogether.entity.dao;


import de.raphael.stellwag.spring.meettogether.entity.model.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    List<MessageEntity> findByEventId(String eventId);

    List<MessageEntity> findByEventId(String eventId, Sort sort);

    int countByEventIdAndDateGreaterThan(String id, LocalDateTime lastReadMessageDate);

    @Query(value = "{ 'eventId': [0] }", sort = "{ date : -1 }")
    List<MessageEntity> getNewestEntityForEventId(String id, Pageable pageable);

    List<MessageEntity> findByUserId(String userId);

}
