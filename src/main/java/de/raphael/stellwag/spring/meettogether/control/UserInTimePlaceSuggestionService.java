package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.spring.meettogether.entity.dao.UserInTimePlaceSuggestionRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.UserInTimePlaceSuggestionEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserInTimePlaceSuggestionService {

    private final UserInTimePlaceSuggestionRepository userInTimePlaceRepository;

    @Autowired
    public UserInTimePlaceSuggestionService(UserInTimePlaceSuggestionRepository userInTimePlaceRepository) {
        this.userInTimePlaceRepository = userInTimePlaceRepository;

    }

    public boolean isUserInTimePlaceSuggestion(String userId, String timePlaceId) {
        Optional<UserInTimePlaceSuggestionEntity> optional = userInTimePlaceRepository.findByUserIdAndTimePlaceSuggestionId(userId, timePlaceId);
        return optional.isPresent();
    }

    public void addUserToTimePlaceSuggestion(String userId, String timePlaceId) {
        UserInTimePlaceSuggestionEntity entity = new UserInTimePlaceSuggestionEntity();
        entity.setTimePlaceSuggestionId(timePlaceId);
        entity.setUserId(userId);
        userInTimePlaceRepository.save(entity);
    }

    public List<String> getParticipantIds(String timePlaceId) {
        List<UserInTimePlaceSuggestionEntity> entities = userInTimePlaceRepository.findByTimePlaceSuggestionId(timePlaceId);
        List<String> ids = new ArrayList<>(entities.size());
        for (UserInTimePlaceSuggestionEntity userInTimePlace :
                entities) {
            ids.add(userInTimePlace.getUserId());
        }
        return ids;
    }

    public void removeUserFromTimePlaceSuggestion(String userId, String timePlaceId) {
        Optional<UserInTimePlaceSuggestionEntity> optionalEntity = userInTimePlaceRepository.findByUserIdAndTimePlaceSuggestionId(userId, timePlaceId);
        if (optionalEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_TIME_PLACE_SUGGESTION);
        }
        userInTimePlaceRepository.delete(optionalEntity.get());
    }
}
