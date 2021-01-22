package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.MessageApi;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.MessagesDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageApiImpl implements MessageApi {

    @Override
    public ResponseEntity<MessageDto> addMessage(String authorization, String eventId, String userId, @Valid MessageDto message) {
        return null;
    }

    @Override
    public ResponseEntity<MessagesDto> getMessages(String authorization, String eventId, @Valid String count, @Valid String lastMessage, @Valid String direction) {
        return null;
    }
}
