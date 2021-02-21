package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.MessageApi;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.MessagesDto;
import de.raphael.stellwag.spring.meettogether.control.EventService;
import de.raphael.stellwag.spring.meettogether.control.MessageService;
import de.raphael.stellwag.spring.meettogether.control.UserInEventService;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageApiImpl implements MessageApi {

    private final UserInEventService userInEventService;
    private final MessageService messageService;
    private final CurrentUser currentUser;

    @Autowired
    public MessageApiImpl(JwtTokenUtil jwtTokenUtil, EventService eventService, UserInEventService userInEventService, MessageService messageService, CurrentUser currentUser) {
        this.userInEventService = userInEventService;
        this.messageService = messageService;
        this.currentUser = currentUser;
    }

    @Override
    public ResponseEntity<MessageDto> addMessage(String eventId, String userId, @Valid MessageDto message) {
        if (!userId.equals(currentUser.getUserName())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        MessageDto messageDto = this.messageService.userSendNewMessage(userId, eventId, message);

        return ResponseEntity.ok(messageDto);
    }

    @Override
    public ResponseEntity<MessagesDto> getMessages(String eventId, @Valid String count, @Valid String lastMessage, @Valid String direction) {

        String userId = currentUser.getUserName();

        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        MessagesDto messageDtoList = this.messageService.getMessages(userId, eventId, count, lastMessage, direction);

        String messageId = messageDtoList.get(messageDtoList.size() - 1).getId();
        userInEventService.setLastReadMessage(userId, eventId, messageId);

        return ResponseEntity.ok(messageDtoList);
    }
}
