package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.MessagesDto;
import de.raphael.stellwag.spring.meettogether.entity.dao.MessageRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageEntity;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import de.raphael.stellwag.spring.meettogether.websocket.WebsocketEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final DtoToEntity dtoToEntity;
    private final UserService userService;
    private final EntityToDto entityToDto;
    private final UserInEventService userInEventService;
    private final WebsocketEndpoint websocketEndpoint;

    @Autowired
    public MessageService(MessageRepository messageRepository, DtoToEntity dtoToEntity, UserService userService, EntityToDto entityToDto, UserInEventService userInEventService, WebsocketEndpoint websocketEndpoint) {
        this.messageRepository = messageRepository;
        this.dtoToEntity = dtoToEntity;
        this.userService = userService;
        this.entityToDto = entityToDto;
        this.userInEventService = userInEventService;
        this.websocketEndpoint = websocketEndpoint;
    }

    public MessageDto userSendNewMessage(String userId, String eventId, MessageDto message) {
        String userName = userService.getUserName(userId);
        MessageEntity messageEntity = dtoToEntity.getMessageEntity(message, userId, userName, eventId);

        MessageEntity insertedEntity = messageRepository.insert(messageEntity);

        MessageDto messageDto = entityToDto.getMessageDto(insertedEntity);
        sendWebsocketMessagesInNewThread(messageDto, eventId);

        return messageDto;
    }


    public MessagesDto getMessages(String userId, String eventId, String count, String lastMessage, String direction) {

        Sort sort = Sort.by("date").ascending();
        List<MessageEntity> messageEntities = messageRepository.findByEventId(eventId, sort);

        if (messageEntities.isEmpty()) {
            return new MessagesDto();
        }

        userInEventService.setLastReadMessage(userId, eventId, messageEntities.get(messageEntities.size() - 1).getId());

        MessagesDto messageDtos = new MessagesDto();
        messageEntities.forEach(e -> messageDtos.add(entityToDto.getMessageDto(e)));

        return messageDtos;
    }

    public void sendGeneratedMessage(MessageTypeEnum messageType, String eventId, String userId) {
        String userName = userService.getUserName(userId);

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setUserName(userName);
        messageEntity.setMessageType(messageType);
        messageEntity.setEventId(eventId);
        messageEntity.setDate(LocalDateTime.now());
        messageEntity.setUserId(userId);
        // TODO: messageEntity.setContent();

        messageEntity = messageRepository.insert(messageEntity);

        MessageDto messageDto = entityToDto.getMessageDto(messageEntity);
        sendWebsocketMessagesInNewThread(messageDto, eventId);
    }

    private void sendWebsocketMessagesInNewThread(MessageDto messageDto, String eventId) {
        Runnable runnable = () -> {
            List<String> userIdsFromEvent = userInEventService.getUserIdsFromEvent(eventId);
            for (String userId : userIdsFromEvent) {
                websocketEndpoint.sendNewMessageToClient(messageDto, userId);
            }
        };

        Thread newThread = new Thread(runnable);
        newThread.start();
    }

}
