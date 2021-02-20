package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.EventApi;
import de.raphael.stellwag.generated.dto.ApiResponseDto;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.EventsDto;
import de.raphael.stellwag.generated.dto.ParticipantsDto;
import de.raphael.stellwag.spring.meettogether.control.EventService;
import de.raphael.stellwag.spring.meettogether.control.MessageService;
import de.raphael.stellwag.spring.meettogether.control.ParticipantService;
import de.raphael.stellwag.spring.meettogether.control.UserInEventService;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final ParticipantService participantService;
    private final MessageService messageService;

    @Autowired
    EventApiImpl(EventService eventService, UserInEventService userInEventService, JwtTokenUtil jwtTokenUtil, ParticipantService participantService, MessageService messageService) {
        this.eventService = eventService;
        this.userInEventService = userInEventService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.participantService = participantService;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public ResponseEntity<EventDto> addEvent(String userId, String authorization, @Valid EventDto body) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventDto newEvent = eventService.createNewEvent(userId, body);

        messageService.sendGeneratedMessage(MessageTypeEnum.EVENT_CREATED, newEvent.getId(), userId);

        return ResponseEntity.created(URI.create("/api/v1/events/" + newEvent.getId())).body(newEvent);
    }

    @Override
    public ResponseEntity<EventDto> addUserToEvent(String authorization, String userId, String eventId, @NotNull @Valid String accesstoken) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId) ||
                !eventService.isAccessTokenCorrect(eventId, accesstoken)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        userInEventService.addUserToEvent(userId, eventId);
        EventDto eventDto = eventService.getEvent(eventId, userId);

        messageService.sendGeneratedMessage(MessageTypeEnum.USER_JOINED_EVENT, eventId, userId);

        return ResponseEntity.ok(eventDto);
    }

    //TODO implementation needed => not yet used
    @Override
    public ResponseEntity<ApiResponseDto> deleteEvent(String authorization, String userId, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteUserFromEvent(String authorization, String userId, String eventId) {
        log.info("Delete user {} from event {}", userId, eventId);
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        userInEventService.deleteUserFromEvent(userId, eventId);

        messageService.sendGeneratedMessage(MessageTypeEnum.USER_LEFT_EVENT, eventId, userId);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ParticipantsDto> getAllParticipants(String authorization, String eventId) {
        String userId = jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getTokenFromHeader(authorization));
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }
        ParticipantsDto participantsDto = participantService.getParticipantsOfEvent(eventId);
        return ResponseEntity.ok(participantsDto);
    }

    @Override
    public ResponseEntity<EventsDto> getEvents(String userId, String authorization) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventsDto eventDtos = eventService.getEvents(userId);
        return ResponseEntity.ok(eventDtos);
    }

    @Override
    public ResponseEntity<EventDto> updateEvent(String authorization, String userId, String eventId, @Valid EventDto eventData) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId) ||
                !eventService.hasUserCreatedEvent(userId, eventId) ||
                !eventData.getId().equals(eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        EventDto eventDto = eventService.updateEvent(eventData, userId);
        eventService.sendEventUpdateToWebsocketClients(eventDto);

        messageService.sendGeneratedMessage(MessageTypeEnum.EVENT_UPDATED, eventId, userId);

        return ResponseEntity.ok(eventDto);
    }
}
