package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.generated.dto.MessagesDto;
import de.raphael.stellwag.spring.meettogether.entity.dao.MessageRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageEntity;
import de.raphael.stellwag.spring.meettogether.entity.model.MessageTypeEnum;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import de.raphael.stellwag.spring.meettogether.websocket.WebsocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final DtoToEntity dtoToEntity;
    private final UserService userService;
    private final EntityToDto entityToDto;
    private final WebsocketEndpoint websocketEndpoint;

    @Autowired
    public MessageService(MessageRepository messageRepository, DtoToEntity dtoToEntity, UserService userService, EntityToDto entityToDto, WebsocketEndpoint websocketEndpoint) {
        this.messageRepository = messageRepository;
        this.dtoToEntity = dtoToEntity;
        this.userService = userService;
        this.entityToDto = entityToDto;
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

        MessagesDto messageDtos = new MessagesDto();
        messageEntities.forEach(e -> messageDtos.add(entityToDto.getMessageDto(e)));

        return messageDtos;
    }

    public void sendGeneratedMessage(MessageTypeEnum messageType, String eventId, String userId, String content) {
        String userName = userService.getUserName(userId);

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setUserName(userName);
        messageEntity.setMessageType(messageType);
        messageEntity.setEventId(eventId);
        messageEntity.setDate(LocalDateTime.now());
        messageEntity.setUserId(userId);
        messageEntity.setContent(content);

        messageEntity = messageRepository.insert(messageEntity);

        MessageDto messageDto = entityToDto.getMessageDto(messageEntity);
        sendWebsocketMessagesInNewThread(messageDto, eventId);
    }

    public LocalDateTime getMessageCreationDate(String messageId) {
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);
        if (messageEntity.isEmpty()) {
            log.warn("Message Entity with id {} does not exist", messageId);
            return null;
        }
        return messageEntity.get().getDate();
    }

    private void sendWebsocketMessagesInNewThread(MessageDto messageDto, String eventId) {
        Runnable runnable = () -> {
            websocketEndpoint.sendNewMessageToClient(messageDto, eventId);
        };

        Thread newThread = new Thread(runnable);
        newThread.start();
    }

    public Integer getCountFromDate(String id, LocalDateTime lastReadMessageDate) {
        return messageRepository.countByEventIdAndDateGreaterThan(id, lastReadMessageDate);
    }

    public LocalDateTime getNewestEntityDateForEvent(String id) {
        List<MessageEntity> messageEntities = messageRepository.getNewestEntityForEventId(id, PageRequest.of(0, 1));
        if (messageEntities.size() != 1) {
            log.warn("Something went wrong");
            return null;
        }
        return messageEntities.get(0).getDate();
    }

}
