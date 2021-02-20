package de.raphael.stellwag.spring.meettogether.entity.dao;


import de.raphael.stellwag.spring.meettogether.entity.model.MessageEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    List<MessageEntity> findByEventId(String eventId, Sort sort);
}
