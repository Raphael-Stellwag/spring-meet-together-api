package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.EventApi;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.EventsDto;
import de.raphael.stellwag.generated.dto.ParticipantsDto;
import de.raphael.stellwag.spring.meettogether.control.*;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Slf4j
@Controller
public class EventApiImpl implements EventApi {

    private final EventService eventService;
    private final UserInEventService userInEventService;
    private final ParticipantService participantService;
    private final MessageService messageService;
    private final CurrentUser currentUser;
    private final TimePlaceSuggestionService timePlaceSuggestionService;

    @Autowired
    EventApiImpl(EventService eventService, UserInEventService userInEventService,
                 ParticipantService participantService, MessageService messageService,
                 CurrentUser currentUser, TimePlaceSuggestionService timePlaceSuggestionService) {
        this.eventService = eventService;
        this.userInEventService = userInEventService;
        this.participantService = participantService;
        this.messageService = messageService;
        this.currentUser = currentUser;
        this.timePlaceSuggestionService = timePlaceSuggestionService;
    }

    @Override
    @Transactional
    public ResponseEntity<EventDto> addEvent(String userId, @Valid EventDto body) {
        if (!userId.equals(currentUser.getUserName())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventDto newEvent = eventService.createNewEvent(userId, body);
        messageService.sendGeneratedMessage(MessageTypeEnum.EVENT_CREATED, newEvent.getId(), userId, newEvent);

        if (body.getStartDate() != null) {
            newEvent = timePlaceSuggestionService.createTimePlaceSuggestionFromEventDto
                    (newEvent.getId(), userId, body);
        }

        return ResponseEntity.created(URI.create("/api/v1/events/" + newEvent.getId())).body(newEvent);
    }

    @Override
    public ResponseEntity<EventDto> addUserToEvent(String userId, String eventId, @NotNull @Valid String accesstoken) {
        if (!userId.equals(currentUser.getUserName()) ||
                !eventService.isAccessTokenCorrect(eventId, accesstoken)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        userInEventService.addUserToEvent(userId, eventId);
        EventDto eventDto = eventService.getEvent(eventId, userId);

        messageService.sendGeneratedMessage(MessageTypeEnum.USER_JOINED_EVENT, eventId, userId, eventDto);

        return ResponseEntity.ok(eventDto);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(String userId, String eventId) {
        if (!userId.equals(currentUser.getUserName()) ||
                !eventService.hasUserCreatedEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteUserFromEvent(String userId, String eventId) {
        log.info("Delete user {} from event {}", userId, eventId);
        if (!userId.equals(currentUser.getUserName())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        userInEventService.deleteUserFromEvent(userId, eventId);

        messageService.sendGeneratedMessage(MessageTypeEnum.USER_LEFT_EVENT, eventId, userId, new Object());

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ParticipantsDto> getAllParticipants(String eventId) {
        String userId = currentUser.getUserName();
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }
        ParticipantsDto participantsDto = participantService.getParticipantsOfEvent(eventId);
        return ResponseEntity.ok(participantsDto);
    }

    @Override
    public ResponseEntity<EventsDto> getEvents(String userId) {
        if (!userId.equals(currentUser.getUserName())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventsDto eventDtos = eventService.getEvents(userId);
        return ResponseEntity.ok(eventDtos);
    }

    @Override
    public ResponseEntity<EventDto> updateEvent(String userId, String eventId, @Valid EventDto eventData) {
        if (!userId.equals(currentUser.getUserName()) ||
                !eventService.hasUserCreatedEvent(userId, eventId) ||
                !eventData.getId().equals(eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventDto eventDto = eventService.updateEvent(eventData, userId);
        eventService.sendEventUpdateToWebsocketClients(eventDto);

        messageService.sendGeneratedMessage(MessageTypeEnum.EVENT_UPDATED, eventId, userId, (eventDto));

        return ResponseEntity.ok(eventDto);
    }
}
