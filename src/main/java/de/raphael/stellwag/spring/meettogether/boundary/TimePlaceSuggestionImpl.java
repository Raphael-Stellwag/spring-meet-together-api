package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.TimePlaceSuggestionApi;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionsDto;
import de.raphael.stellwag.spring.meettogether.control.*;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class TimePlaceSuggestionImpl implements TimePlaceSuggestionApi {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final EventService eventService;
    private final UserInEventService userInEventService;
    private final TimePlaceSuggestionService timePlaceSuggestionService;
    private final UserInTimePlaceSuggestionService userInTimePlaceSuggestionService;

    @Autowired
    public TimePlaceSuggestionImpl(JwtTokenUtil jwtTokenUtil, UserService userService, EventService eventService, UserInEventService userInEventService, TimePlaceSuggestionService timePlaceSuggestionService, UserInTimePlaceSuggestionService userInTimePlaceSuggestionService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.eventService = eventService;
        this.userInEventService = userInEventService;
        this.timePlaceSuggestionService = timePlaceSuggestionService;
        this.userInTimePlaceSuggestionService = userInTimePlaceSuggestionService;
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> addUserToTimePlaceSuggestion(String authorization, String eventId, String timePlaceId, String userId) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
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
    public ResponseEntity<TimePlaceSuggestionDto> createsTimePlaceSuggestions(String authorization, String eventId, @Valid TimePlaceSuggestionDto timePlaceSuggestion) {
        String userId = this.jwtTokenUtil.getUsernameFromToken(this.jwtTokenUtil.getTokenFromHeader(authorization));
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        TimePlaceSuggestionDto timePlaceSuggestionDto = timePlaceSuggestionService.createTimePlaceSuggestion(eventId, timePlaceSuggestion);
        return ResponseEntity.ok(timePlaceSuggestionDto);
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> deleteUserFromTimePlaceSuggestion(String authorization, String eventId, String timePlaceId, String userId) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
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
    public ResponseEntity<TimePlaceSuggestionsDto> getAllTimePlaceSuggestions(String authorization, String eventId) {
        String userId = this.jwtTokenUtil.getUsernameFromToken(this.jwtTokenUtil.getTokenFromHeader(authorization));
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        TimePlaceSuggestionsDto timePlaceSuggestionDtos = timePlaceSuggestionService.getAllForOneEvent(eventId);
        return ResponseEntity.ok(timePlaceSuggestionDtos);
    }

    @Override
    public ResponseEntity<EventDto> timePlaceSuggestionChoosen(String authorization, String eventId, String timePlaceId, String userId) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        if (!eventService.hasUserCreatedEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_CREATOR_OF_EVENT);
        }
        if (!timePlaceSuggestionService.timePlaceSuggestionBelongsToEvent(timePlaceId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.TIME_PLACE_SUGGESTION_BELONGS_NOT_TO_EVENT);
        }

        EventDto eventDto = timePlaceSuggestionService.timePlaceIdWasChoosen(timePlaceId, eventId);
        return ResponseEntity.ok(eventDto);
    }
}
