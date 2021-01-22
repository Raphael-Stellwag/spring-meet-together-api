package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.EventApi;
import de.raphael.stellwag.generated.dto.ApiResponseDto;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.EventsDto;
import de.raphael.stellwag.generated.dto.ParticipantsDto;
import de.raphael.stellwag.spring.meettogether.control.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Controller
public class EventApiImpl implements EventApi {

    @Autowired
    private EventService eventService;

    @Override
    public ResponseEntity<EventDto> addEvent(String userId, String authorization, @Valid EventDto body) {
        EventDto newEvent = eventService.createNewEvent(userId, body);
        return ResponseEntity.created(URI.create("/api/v1/events/" + newEvent.getId())).body(newEvent);
    }

    @Override
    public ResponseEntity<EventDto> addUserToEvent(String authorization, String userId, String eventId, @NotNull @Valid String accesstoken) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseDto> deleteEvent(String authorization, String userId, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteUserFromEvent(String authorization, String userId, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<ParticipantsDto> getAllParticipants(String authorization, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<EventsDto> getEvents(String userId, String authorization) {
        return null;
    }

    @Override
    public ResponseEntity<EventDto> updateEvent(String authorization, String userId, String eventId, @Valid EventDto eventData) {
        return null;
    }
}
