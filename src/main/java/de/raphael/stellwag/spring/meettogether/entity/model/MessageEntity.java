package de.raphael.stellwag.spring.meettogether.entity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "message")
public class MessageEntity {

    @Id
    private String id;

    private String eventId;
    private String userId;
    private String userName;

    private LocalDateTime date;
    private String content;
    private MessageTypeEnum messageType;
}
