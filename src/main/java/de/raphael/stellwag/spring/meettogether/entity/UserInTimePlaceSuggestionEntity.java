package de.raphael.stellwag.spring.meettogether.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document("UserInTimePlaceSuggestion")
public class UserInTimePlaceSuggestionEntity {

    @Id
    private String id;

    private String timePlaceSuggestionId;
    private String userId;
}
