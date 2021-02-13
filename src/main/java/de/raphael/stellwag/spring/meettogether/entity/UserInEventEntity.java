package de.raphael.stellwag.spring.meettogether.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document("UserInEvent")
public class UserInEventEntity {
    @Id
    private String id;

    private String userId;
    private String eventId;
}
