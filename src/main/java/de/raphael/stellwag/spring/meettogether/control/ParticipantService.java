package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.ParticipantDto;
import de.raphael.stellwag.generated.dto.ParticipantsDto;
import de.raphael.stellwag.spring.meettogether.entity.model.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.model.UserEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ParticipantService {

    private final UserInEventService userInEventService;
    private final EventService eventService;
    private final UserService userService;
    private final EntityToDto entityToDto;
    private final UserInTimePlaceSuggestionService userInTimePlaceSuggestionService;

    @Autowired
    public ParticipantService(UserInEventService userInEventService, EventService eventService, UserService userService, EntityToDto entityToDto, UserInTimePlaceSuggestionService userInTimePlaceSuggestionService) {
        this.userInEventService = userInEventService;
        this.eventService = eventService;
        this.userService = userService;
        this.entityToDto = entityToDto;
        this.userInTimePlaceSuggestionService = userInTimePlaceSuggestionService;
    }

    public ParticipantsDto getParticipantsOfEvent(String eventId) {
        List<String> ids = userInEventService.getUserIdsFromEvent(eventId);
        Iterable<UserEntity> users = userService.getUserEntities(ids);
        EventEntity event = eventService.getEventEntity(eventId);
        return entityToDto.getParticipantsDto(users, event.getCreatorId());
    }

    public List<ParticipantDto> getParticipantsOfTimePlaceSuggestion(String timePlaceId, String eventId) {
        List<String> ids = userInTimePlaceSuggestionService.getParticipantIds(timePlaceId);
        Iterable<UserEntity> users = userService.getUserEntities(ids);
        EventEntity event = eventService.getEventEntity(eventId);
        return entityToDto.getParticipantsDto(users, event.getCreatorId());
    }
}
