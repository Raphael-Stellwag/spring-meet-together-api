package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.spring.meettogether.entity.UserInEventEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserInEventRepository;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserInEventService {

    private final UserInEventRepository userInEventRepository;

    @Autowired
    public UserInEventService(UserInEventRepository userInEventRepository) {
        this.userInEventRepository = userInEventRepository;
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
        UserInEventEntity userInEventEntity = new UserInEventEntity(null, userId, eventId);
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
}
