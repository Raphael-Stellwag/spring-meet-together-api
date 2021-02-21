package de.raphael.stellwag.spring.meettogether.websocket.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketRequestAdditionalData {
    private String eventId;
    private String userId;
    private String messageId;
}
