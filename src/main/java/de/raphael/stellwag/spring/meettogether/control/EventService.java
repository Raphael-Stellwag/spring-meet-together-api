package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.spring.meettogether.entity.EventEntity;
import de.raphael.stellwag.spring.meettogether.entity.EventRepository;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final DtoToEntity dtoToEntity;
    private final EntityToDto entityToDto;

    @Autowired
    EventService(EventRepository eventRepository, DtoToEntity dtoToEntity, EntityToDto entityToDto) {
        this.eventRepository = eventRepository;
        this.dtoToEntity = dtoToEntity;
        this.entityToDto = entityToDto;
    }

    public EventDto createNewEvent(String UserId, EventDto eventData) {
        log.info(eventData.toString());
        EventEntity newEntity = dtoToEntity.getEventEntity(eventData);
        EventEntity writtenEntity = this.eventRepository.insert(newEntity);
        return entityToDto.getEventDto(writtenEntity);
    }


}
