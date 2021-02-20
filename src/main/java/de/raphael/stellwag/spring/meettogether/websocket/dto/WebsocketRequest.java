package de.raphael.stellwag.spring.meettogether.websocket.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketRequest {
    private WebsocketRequestMethod method;
    private String token;
    private String additionalData;
}