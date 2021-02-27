package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.spring.meettogether.entity.dao.UserInEventRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.UserInEventEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserInEventService {

    private final UserInEventRepository userInEventRepository;
    private final MessageService messageService;

    @Autowired
    public UserInEventService(UserInEventRepository userInEventRepository, MessageService messageService) {
        this.userInEventRepository = userInEventRepository;
        this.messageService = messageService;
    }


    public List<String> getEventIdsFromUser(String userId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByUserId(userId);
        List<String> ids = new ArrayList<>(userInEventEntities.size());
        for (UserInEventEntity userInEventEntity :
                userInEventEntities) {
            ids.add(userInEventEntity.getEventId());
        }
        return ids;
    }

    public void addUserToEvent(String userId, String eventId) {
        UserInEventEntity userInEventEntity = new UserInEventEntity();
        userInEventEntity.setEventId(eventId);
        userInEventEntity.setUserId(userId);
        userInEventRepository.insert(userInEventEntity);
    }

    public void deleteUserFromEvent(String userId, String eventId) {
        if (!isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }
        userInEventRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    public boolean isUserInEvent(String userId, String eventId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByUserIdAndEventId(userId, eventId);
        return (!userInEventEntities.isEmpty());
    }

    public List<String> getUserIdsFromEvent(String eventId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByEventId(eventId);
        List<String> ids = new ArrayList<>(userInEventEntities.size());
        for (UserInEventEntity userInEventEntity :
                userInEventEntities) {
            ids.add(userInEventEntity.getUserId());
        }
        return ids;
    }

    public void setLastReadMessage(String userId, String eventId, String messageId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByUserIdAndEventId(userId, eventId);
        if (userInEventEntities.size() > 1) {
            log.warn("User is more than one time in an event !!!!!!");
        }
        LocalDateTime lastReadMessageDate = messageService.getMessageCreationDate(messageId);
        for (UserInEventEntity userInEventEntity : userInEventEntities) {
            userInEventEntity.setLastReadMessageId(messageId);
            userInEventEntity.setLastReadMessageTime(lastReadMessageDate);
            userInEventRepository.save(userInEventEntity);
        }
    }

    public List<UserInEventEntity> getEntitiesFromUser(String userId) {
        return userInEventRepository.findByUserId(userId);
    }

    public LocalDateTime getLastReadMessageDate(String eventId, String userId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByUserIdAndEventId(userId, eventId);
        if (userInEventEntities == null || userInEventEntities.isEmpty()) {
            log.warn("No User there !!!!!!");
            return null;
        }
        if (userInEventEntities.size() > 1) {
            log.warn("User is more than one time in an event !!!!!!");
        }
        return userInEventEntities.get(0).getLastReadMessageTime();
    }

    public void deleteAllUsersFromEvent(String eventId) {
        List<UserInEventEntity> userInEventEntities = userInEventRepository.findByEventId(eventId);
        if (!userInEventEntities.isEmpty()) {
            userInEventRepository.deleteAll(userInEventEntities);
        }
    }
}
