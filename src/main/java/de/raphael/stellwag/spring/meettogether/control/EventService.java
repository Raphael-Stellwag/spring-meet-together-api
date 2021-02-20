package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.EventsDto;
import de.raphael.stellwag.spring.meettogether.entity.dao.EventRepository;
import de.raphael.stellwag.spring.meettogether.entity.dao.UserInEventRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.model.TimePlaceSuggestionEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import de.raphael.stellwag.spring.meettogether.websocket.WebsocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final UserInEventService userInEventService;
    private final DtoToEntity dtoToEntity;
    private final EntityToDto entityToDto;
    private final WebsocketEndpoint websocketEndpoint;

    @Autowired
    EventService(EventRepository eventRepository, UserInEventRepository userInEventRepository, UserInEventService userInEventService, DtoToEntity dtoToEntity, EntityToDto entityToDto, WebsocketEndpoint websocketEndpoint) {
        this.eventRepository = eventRepository;
        this.userInEventService = userInEventService;
        this.dtoToEntity = dtoToEntity;
        this.entityToDto = entityToDto;
        this.websocketEndpoint = websocketEndpoint;
    }

    public EventDto createNewEvent(String userId, EventDto eventData) {
        log.info(eventData.toString());
        EventEntity newEntity = dtoToEntity.getEventEntity(eventData);
        newEntity.setCreatorId(userId);
        EventEntity writtenEntity = this.eventRepository.insert(newEntity);

        userInEventService.addUserToEvent(userId, writtenEntity.getId());

        return entityToDto.getEventDto(writtenEntity, userId);
    }

    public EventsDto getEvents(String userId) {
        List<String> ids = userInEventService.getEventIdsFromUser(userId);

        Iterable<EventEntity> eventEntities = eventRepository.findAllById(ids);

        EventsDto eventDtos = new EventsDto();
        for (EventEntity eventEntity :
                eventEntities) {
            eventDtos.add(entityToDto.getEventDto(eventEntity, userId));
        }

        return eventDtos;
    }

    public boolean isAccessTokenCorrect(String eventId, String accesstoken) {
        EventEntity entity = getEventEntity(eventId);
        return accesstoken.equals(entity.getAccesstoken());
    }

    protected EventEntity getEventEntity(String eventId) {
        Optional<EventEntity> optionalEventEntity = eventRepository.findById(eventId);
        if (optionalEventEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.EVENT_NOT_FOUND);
        }
        return optionalEventEntity.get();
    }

    public EventDto getEvent(String eventId, String userId) {
        return entityToDto.getEventDto(getEventEntity(eventId), userId);
    }

    public boolean hasUserCreatedEvent(String userId, String eventId) {
        EventEntity entity = getEventEntity(eventId);
        return entity.getCreatorId().equals(userId);
    }

    public EventDto updateEvent(EventDto eventData, String userId) {
        EventEntity eventEntity = dtoToEntity.getEventEntity(eventData);
        eventEntity.setCreatorId(userId);

        Optional<EventEntity> optionalEventEntity = eventRepository.findById(eventData.getId());

        optionalEventEntity.ifPresent(entity -> eventEntity.setChosenTimePlaceSuggestionEntity(entity.getChosenTimePlaceSuggestionEntity()));

        EventEntity writtenEntity = eventRepository.save(eventEntity);
        EventDto eventDto = entityToDto.getEventDto(writtenEntity, userId);

        sendWebsocketMessagesInNewThread(eventDto);
        return eventDto;
    }

    public EventDto timePlaceWasChoosen(String eventId, TimePlaceSuggestionEntity timePlaceSuggestionEntity) {
        EventEntity eventEntity = getEventEntity(eventId);
        eventEntity.setChosenTimePlaceSuggestionEntity(timePlaceSuggestionEntity);
        EventEntity writtenEntity = eventRepository.save(eventEntity);
        return entityToDto.getEventDto(writtenEntity, eventEntity.getCreatorId());
    }

    private void sendWebsocketMessagesInNewThread(EventDto eventDto) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<String> userIdsFromEvent = userInEventService.getUserIdsFromEvent(eventDto.getId());
                for (String userId : userIdsFromEvent) {
                    websocketEndpoint.sendEventUpdateToClient(eventDto, userId);
                }
            }
        };

        Thread newThread = new Thread(runnable);
        newThread.start();
    }
}
