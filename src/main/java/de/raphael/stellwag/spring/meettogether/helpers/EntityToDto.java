package de.raphael.stellwag.spring.meettogether.helpers;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.entity.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToDto {

    public EventDto getEventDto(EventEntity writtenEntity) {
        EventDto eventDto = new EventDto();
        eventDto.setAccesstoken(writtenEntity.getAccesstoken());
        eventDto.setDescription(writtenEntity.getDescription());
        eventDto.setId(writtenEntity.getId());
        eventDto.setName(writtenEntity.getName());
        return eventDto;
    }

    public UserDto getUserDto(UserEntity writtenEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(writtenEntity.getId());
        userDto.setEmail(writtenEntity.getEmail());
        userDto.setName(writtenEntity.getName());
        userDto.setRegistered(writtenEntity.isRegistered());
        return userDto;
    }
}
