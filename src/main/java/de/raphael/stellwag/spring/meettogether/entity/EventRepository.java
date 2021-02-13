package de.raphael.stellwag.spring.meettogether.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
}
