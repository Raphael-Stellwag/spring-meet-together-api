package de.raphael.stellwag.spring.meettogether.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event")
public class EventEntity {

    @Id
    private String id;

    private String name;
    private String description;

    @Indexed(unique = true)
    private String accesstoken;

    private String creatorId;

    private TimePlaceSuggestionEntity chosenTimePlaceSuggestionEntity;

}
