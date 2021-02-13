package de.raphael.stellwag.spring.meettogether.helpers;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.entity.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.TimePlaceSuggestionEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserEntity;
import org.springframework.stereotype.Component;

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
}
