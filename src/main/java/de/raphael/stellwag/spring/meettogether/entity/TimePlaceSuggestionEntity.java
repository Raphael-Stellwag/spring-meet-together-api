package de.raphael.stellwag.spring.meettogether.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Document("TimePlaceSuggestion")
public class TimePlaceSuggestionEntity {

    @Id
    private String id;

    private String eventId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String place;
    private String link;

}
