package de.raphael.stellwag.spring.meettogether.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInEventRepository extends MongoRepository<UserInEventEntity, String> {

    List<UserInEventEntity> findByUserId(String userId);

    List<UserInEventEntity> findByUserIdAndEventId(String userId, String eventId);

    void deleteByUserIdAndEventId(String userId, String eventId);

    List<UserInEventEntity> findByEventId(String eventId);
}
