package de.raphael.stellwag.spring.meettogether.websocket.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketResponse {
    private WebsocketResponseMethod method;
    private String token;
    private String additionalData;
}
