package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionsDto;
import de.raphael.stellwag.spring.meettogether.entity.dao.TimePlaceSuggestionRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.entity.model.TimePlaceSuggestionEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TimePlaceSuggestionService {
    private final TimePlaceSuggestionRepository timePlaceSuggestionRepository;
    private final EntityToDto entityToDto;
    private final DtoToEntity dtoToEntity;
    private final ParticipantService participantService;
    private final EventService eventService;
    private final MessageService messageService;
    private final UserInTimePlaceSuggestionService userInTimePlaceSuggestionService;

    @Autowired
    public TimePlaceSuggestionService(TimePlaceSuggestionRepository timePlaceSuggestionRepository, EntityToDto entityToDto, DtoToEntity dtoToEntity,
                                      ParticipantService participantService, EventService eventService, MessageService messageService, UserInTimePlaceSuggestionService userInTimePlaceSuggestionService) {
        this.timePlaceSuggestionRepository = timePlaceSuggestionRepository;
        this.entityToDto = entityToDto;
        this.dtoToEntity = dtoToEntity;
        this.participantService = participantService;
        this.eventService = eventService;
        this.messageService = messageService;
        this.userInTimePlaceSuggestionService = userInTimePlaceSuggestionService;
    }

    public TimePlaceSuggestionsDto getAllForOneEvent(String eventId) {
        List<TimePlaceSuggestionEntity> timePlaceSuggestionEntities = timePlaceSuggestionRepository.findByEventId(eventId);
        TimePlaceSuggestionsDto timePlaceDtos = new TimePlaceSuggestionsDto();
        for (TimePlaceSuggestionEntity timePlaceEntity :
                timePlaceSuggestionEntities) {
            TimePlaceSuggestionDto timePlaceDto = entityToDto.getTimePlaceSuggestionDto(timePlaceEntity);
            timePlaceDto.setCanAttend(participantService.getParticipantsOfTimePlaceSuggestion(timePlaceEntity.getId(), eventId));
            timePlaceDtos.add(timePlaceDto);
        }
        return timePlaceDtos;
    }

    public TimePlaceSuggestionDto createTimePlaceSuggestion(String eventId, TimePlaceSuggestionDto timePlaceSuggestion) {
        TimePlaceSuggestionEntity entity = dtoToEntity.getTimePlaceSuggestionEntity(timePlaceSuggestion, eventId);
        TimePlaceSuggestionEntity writtenEntity = timePlaceSuggestionRepository.save(entity);
        return entityToDto.getTimePlaceSuggestionDto(writtenEntity);
    }

    public TimePlaceSuggestionDto getTimePlaceSuggestionDto(String timePlaceId) {
        Optional<TimePlaceSuggestionEntity> optional = timePlaceSuggestionRepository.findById(timePlaceId);
        if (optional.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.TIME_PLACE_SUGGESTION_DOES_NOT_EXIST);
        }
        TimePlaceSuggestionDto timePlaceSuggestionDto = entityToDto.getTimePlaceSuggestionDto(optional.get());
        timePlaceSuggestionDto.setCanAttend(participantService.getParticipantsOfTimePlaceSuggestion(timePlaceId, optional.get().getEventId()));
        return timePlaceSuggestionDto;
    }

    public boolean timePlaceSuggestionBelongsToEvent(String timePlaceId, String eventId) {
        Optional<TimePlaceSuggestionEntity> optional = timePlaceSuggestionRepository.findById(timePlaceId);
        if (optional.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.TIME_PLACE_SUGGESTION_DOES_NOT_EXIST);
        }

        return eventId.equals(optional.get().getEventId());
    }

    public EventDto timePlaceIdWasChoosen(String timePlaceId, String eventId) {
        Optional<TimePlaceSuggestionEntity> optional = timePlaceSuggestionRepository.findById(timePlaceId);
        if (optional.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.TIME_PLACE_SUGGESTION_DOES_NOT_EXIST);
        }
        return eventService.timePlaceWasChoosen(eventId, optional.get());
    }

    public EventDto createTimePlaceSuggestionFromEventDto(String eventId, String userId, EventDto eventData) {
        TimePlaceSuggestionEntity timePlaceSuggestionEntity = dtoToEntity.getTimePlaceSuggestionEntity(eventData, eventId);
        TimePlaceSuggestionEntity writtenEntity = timePlaceSuggestionRepository.save(timePlaceSuggestionEntity);
        messageService.sendGeneratedMessage(MessageTypeEnum.TIME_PLACE_SUGGESTION_CHOOSEN,
                eventId, userId, writtenEntity);
        return eventService.timePlaceWasChoosen(eventId, writtenEntity);
    }

    public void deleteAllFromEvent(String eventId) {
        List<TimePlaceSuggestionEntity> timePlaceSuggestionEntities = timePlaceSuggestionRepository.findByEventId(eventId);
        if (!timePlaceSuggestionEntities.isEmpty()) {
            timePlaceSuggestionEntities.forEach(timePlaceSuggestionEntity ->
                    userInTimePlaceSuggestionService.deleteTimePlaceSuggestion(timePlaceSuggestionEntity.getId()));

            timePlaceSuggestionRepository.deleteAll(timePlaceSuggestionEntities);
        }
    }
}
