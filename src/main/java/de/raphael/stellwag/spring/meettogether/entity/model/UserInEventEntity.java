package de.raphael.stellwag.spring.meettogether.entity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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

    private String lastReadMessageId;
    private LocalDateTime lastReadMessageTime;
}
