package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.TimePlaceSuggestionApi;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionsDto;
import de.raphael.stellwag.spring.meettogether.control.*;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class TimePlaceSuggestionImpl implements TimePlaceSuggestionApi {

    private final EventService eventService;
    private final UserInEventService userInEventService;
    private final TimePlaceSuggestionService timePlaceSuggestionService;
    private final UserInTimePlaceSuggestionService userInTimePlaceSuggestionService;
    private final MessageService messageService;
    private final CurrentUser currentUser;

    @Autowired
    public TimePlaceSuggestionImpl(EventService eventService, UserInEventService userInEventService, TimePlaceSuggestionService timePlaceSuggestionService, UserInTimePlaceSuggestionService userInTimePlaceSuggestionService, MessageService messageService, CurrentUser currentUser) {
        this.eventService = eventService;
        this.userInEventService = userInEventService;
        this.timePlaceSuggestionService = timePlaceSuggestionService;
        this.userInTimePlaceSuggestionService = userInTimePlaceSuggestionService;
        this.messageService = messageService;
        this.currentUser = currentUser;
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> addUserToTimePlaceSuggestion(String eventId, String timePlaceId) {
        String userId = currentUser.getUserId();
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }
        if (userInTimePlaceSuggestionService.isUserInTimePlaceSuggestion(userId, timePlaceId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_ALREADY_IN_TIME_PLACE_SUGGESTION);
        }
        userInTimePlaceSuggestionService.addUserToTimePlaceSuggestion(userId, timePlaceId);
        TimePlaceSuggestionDto dto = timePlaceSuggestionService.getTimePlaceSuggestionDto(timePlaceId);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> createsTimePlaceSuggestions(String eventId, @Valid TimePlaceSuggestionDto timePlaceSuggestion) {
        String userId = currentUser.getUserId();
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        TimePlaceSuggestionDto timePlaceSuggestionDto = timePlaceSuggestionService.createTimePlaceSuggestion(eventId, timePlaceSuggestion);

        messageService.sendGeneratedMessage(MessageTypeEnum.TIME_PLACE_SUGGESTION_ADDED, eventId, userId, timePlaceSuggestionDto);

        return ResponseEntity.ok(timePlaceSuggestionDto);
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> deleteUserFromTimePlaceSuggestion(String eventId, String timePlaceId) {
        String userId = currentUser.getUserId();
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }
        if (!userInTimePlaceSuggestionService.isUserInTimePlaceSuggestion(userId, timePlaceId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_TIME_PLACE_SUGGESTION);
        }

        this.userInTimePlaceSuggestionService.removeUserFromTimePlaceSuggestion(userId, timePlaceId);

        TimePlaceSuggestionDto dto = timePlaceSuggestionService.getTimePlaceSuggestionDto(timePlaceId);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionsDto> getAllTimePlaceSuggestions(String eventId) {
        String userId = currentUser.getUserId();
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        TimePlaceSuggestionsDto timePlaceSuggestionDtos = timePlaceSuggestionService.getAllForOneEvent(eventId);
        return ResponseEntity.ok(timePlaceSuggestionDtos);
    }

    @Override
    public ResponseEntity<EventDto> timePlaceSuggestionChoosen(String eventId, String timePlaceId) {
        String userId = currentUser.getUserId();
        if (!eventService.hasUserCreatedEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_CREATOR_OF_EVENT);
        }
        if (!timePlaceSuggestionService.timePlaceSuggestionBelongsToEvent(timePlaceId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.TIME_PLACE_SUGGESTION_BELONGS_NOT_TO_EVENT);
        }

        EventDto eventDto = timePlaceSuggestionService.timePlaceIdWasChoosen(timePlaceId, eventId);

        eventService.sendEventUpdateToWebsocketClients(eventDto);

        TimePlaceSuggestionDto timePlaceSuggestionDto = timePlaceSuggestionService.getTimePlaceSuggestionDto(timePlaceId);
        messageService.sendGeneratedMessage(MessageTypeEnum.TIME_PLACE_SUGGESTION_CHOOSEN,
                eventId, userId, timePlaceSuggestionDto);

        return ResponseEntity.ok(eventDto);
    }
}
