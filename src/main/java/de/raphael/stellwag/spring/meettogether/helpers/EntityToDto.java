package de.raphael.stellwag.spring.meettogether.helpers;

import de.raphael.stellwag.generated.dto.*;
import de.raphael.stellwag.spring.meettogether.entity.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.TimePlaceSuggestionEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToDto {

    public EventDto getEventDto(EventEntity writtenEntity, String userId) {
        EventDto eventDto = new EventDto();
        eventDto.setAccesstoken(writtenEntity.getAccesstoken());
        eventDto.setDescription(writtenEntity.getDescription());
        eventDto.setId(writtenEntity.getId());
        eventDto.setName(writtenEntity.getName());

        if (writtenEntity.getChosenTimePlaceSuggestionEntity() != null) {
            eventDto.setStartDate(writtenEntity.getChosenTimePlaceSuggestionEntity().getStartDate());
            eventDto.setEndDate(writtenEntity.getChosenTimePlaceSuggestionEntity().getEndDate());
        }

        if (writtenEntity.getCreatorId().equals(userId)) {
            eventDto.setCreator(true);
        }
        return eventDto;
    }

    public UserDto getUserDto(UserEntity writtenEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(writtenEntity.getId());
        userDto.setEmail(writtenEntity.getEmail());
        userDto.setName(writtenEntity.getName());
        userDto.setRegistered(writtenEntity.isRegistered());
        return userDto;
    }

    public ParticipantsDto getParticipantsDto(Iterable<UserEntity> users, String creatorId) {
        ParticipantsDto participantDtos = new ParticipantsDto();
        for (UserEntity userEntity :
                users) {
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.setId(userEntity.getId());
            participantDto.setName(userEntity.getName());
            if (userEntity.getId().equals(creatorId)) {
                participantDto.setCreator(true);
            }
            participantDtos.add(participantDto);
        }
        return participantDtos;
    }

    public TimePlaceSuggestionDto getTimePlaceSuggestionDto(TimePlaceSuggestionEntity timePlaceSuggestionEntity) {
        TimePlaceSuggestionDto timePlaceSuggestionDto = new TimePlaceSuggestionDto();
        timePlaceSuggestionDto.setId(timePlaceSuggestionEntity.getId());
        timePlaceSuggestionDto.setPlace(timePlaceSuggestionEntity.getPlace());
        timePlaceSuggestionDto.setLink(timePlaceSuggestionEntity.getLink());
        timePlaceSuggestionDto.setStartDate(timePlaceSuggestionEntity.getStartDate());
        timePlaceSuggestionDto.setEndDate(timePlaceSuggestionEntity.getEndDate());

        return timePlaceSuggestionDto;
    }
}
