package de.raphael.stellwag.spring.meettogether.helpers;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.entity.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DtoToEntity {
    public EventEntity getEventEntity(EventDto eventDto) {
        EventEntity newEntity = new EventEntity();
        newEntity.setName(eventDto.getName());
        newEntity.setDescription(eventDto.getDescription());
        newEntity.setAccesstoken(eventDto.getAccesstoken());
        newEntity.setName(eventDto.getName());
        newEntity.setId(eventDto.getId());

        return newEntity;
    }

    public UserEntity getUserEntity(UserDto body) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(body.getEmail());
        userEntity.setId(body.getId());
        userEntity.setName(body.getName());
        userEntity.setPassword(body.getPassword());
        userEntity.setRegistered(body.isRegistered());
        return userEntity;
    }

    public TimePlaceSuggestionEntity getTimePlaceSuggestionEntity(TimePlaceSuggestionDto timePlaceSuggestion,
                                                                  String eventId) {
        TimePlaceSuggestionEntity entity = new TimePlaceSuggestionEntity();
        entity.setLink(timePlaceSuggestion.getLink());
        entity.setId(timePlaceSuggestion.getId());
        entity.setEndDate(timePlaceSuggestion.getEndDate());
        entity.setStartDate(timePlaceSuggestion.getStartDate());
        entity.setPlace(timePlaceSuggestion.getPlace());
        entity.setEventId(eventId);
        return entity;
    }

    public MessageEntity getMessageEntity(MessageDto message, String userId,
                                          String userName, String eventId) {
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setUserId(userId);
        messageEntity.setUserName(userName);
        messageEntity.setEventId(eventId);
        messageEntity.setContent(message.getContent());
        messageEntity.setDate(LocalDateTime.now());
        messageEntity.setMessageType(MessageTypeEnum.CUSTOM);

        return messageEntity;
    }
}
