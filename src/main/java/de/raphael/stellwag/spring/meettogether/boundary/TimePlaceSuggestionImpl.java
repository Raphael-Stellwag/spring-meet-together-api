package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.TimePlaceSuggestionApi;
import de.raphael.stellwag.generated.dto.ApiResponseDto;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionDto;
import de.raphael.stellwag.generated.dto.TimePlaceSuggestionsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class TimePlaceSuggestionImpl implements TimePlaceSuggestionApi {

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> addUserToTimePlaceSuggestion(String authorization, String eventId, String timePlaceId, String userId) {
        return null;
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionDto> createsTimePlaceSuggestions(String authorization, String eventId, @Valid TimePlaceSuggestionDto timePlaceSuggestion) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseDto> deleteUserFromTimePlaceSuggestion(String authorization, String eventId, String timePlaceId, String userId) {
        return null;
    }

    @Override
    public ResponseEntity<TimePlaceSuggestionsDto> getAllTimePlaceSuggestions(String authorization, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<EventDto> timePlaceSuggestionChoosen(String authorization, String eventId, String timePlaceId, String userId) {
        return null;
    }
}
