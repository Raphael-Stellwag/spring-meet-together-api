package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.MessageApi;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.MessagesDto;
import de.raphael.stellwag.spring.meettogether.control.EventService;
import de.raphael.stellwag.spring.meettogether.control.MessageService;
import de.raphael.stellwag.spring.meettogether.control.UserInEventService;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageApiImpl implements MessageApi {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserInEventService userInEventService;
    private final MessageService messageService;

    @Autowired
    public MessageApiImpl(JwtTokenUtil jwtTokenUtil, EventService eventService, UserInEventService userInEventService, MessageService messageService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userInEventService = userInEventService;
        this.messageService = messageService;
    }

    @Override
    public ResponseEntity<MessageDto> addMessage(String authorizationHeader, String eventId, String userId, @Valid MessageDto message) {
        if (!jwtTokenUtil.headerBelongsToUser(authorizationHeader, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        MessageDto messageDto = this.messageService.userSendNewMessage(userId, eventId, message);

        return ResponseEntity.ok(messageDto);
    }

    //TODO implementation needed
    @Override
    public ResponseEntity<MessagesDto> getMessages(String authorization, String eventId,
                                                   @Valid String count, @Valid String lastMessage, @Valid String direction) {

        String userId = jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getTokenFromHeader(authorization));

        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        MessagesDto messageDtoList = this.messageService.getMessages(userId, eventId, count, lastMessage, direction);

        return ResponseEntity.ok(messageDtoList);
    }
}
