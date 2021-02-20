package de.raphael.stellwag.spring.meettogether.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.raphael.stellwag.generated.dto.EventDto;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import de.raphael.stellwag.spring.meettogether.websocket.dto.WebsocketRequest;
import de.raphael.stellwag.spring.meettogether.websocket.dto.WebsocketResponse;
import de.raphael.stellwag.spring.meettogether.websocket.dto.WebsocketResponseMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/ws",
        configurator = CustomSpringConfigurator.class)
public class WebsocketEndpoint {

    ConcurrentHashMap<String, List<Session>> userSessionHashMap = new ConcurrentHashMap<>();
    @Autowired
    private ObjectMapper om;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        log.info("onOpen");

        Map<String, Object> userProperties = config.getUserProperties();

        userProperties.forEach((s, o) -> {
            log.info("{} has value {}", s, o);
        });

    }

    @SneakyThrows
    @OnMessage
    public void onMessage(String message, Session session) {

        log.info(message);
        WebsocketRequest websocketRequest = null;
        try {
            websocketRequest = om.readValue(message, WebsocketRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw e;
        }

        switch (websocketRequest.getMethod()) {
            case AUTHENTICATE -> {
                log.info("validate Token");
                String userId = jwtTokenUtil.getUsernameFromToken(websocketRequest.getToken());
                log.info("Token is valid: {}", userId);
                this.addUserSessionToHashMap(session, userId);
                WebsocketResponse websocketResponse = new WebsocketResponse();
                websocketResponse.setMethod(WebsocketResponseMethod.OK);
                this.sendWebsocketResponseToClient(websocketResponse, userId);
            }
            case READ_RECEIVED_DATA -> {
                //TODO implement this one
            }
            default -> {
                log.error("websocket request method unknown: {}", websocketRequest.getMethod());
                session.close();
            }
        }

    }

    @OnClose
    public void onClose(Session client, CloseReason reason) {
        log.info("connection was closed");
        log.info("{}: {}", reason.getCloseCode().getCode(), reason.getReasonPhrase());
    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    @SneakyThrows
    public void sendEventUpdateToClient(EventDto updatedEventData, String targetUserId) {
        WebsocketResponse websocketResponse = new WebsocketResponse();
        websocketResponse.setMethod(WebsocketResponseMethod.EVENT_UPDATE);
        websocketResponse.setAdditionalData(om.writeValueAsString(updatedEventData));

        sendWebsocketResponseToClient(websocketResponse, targetUserId);
    }

    @SneakyThrows
    public void sendNewMessageToClient(MessageDto newMessageData, String targetUserId) {
        WebsocketResponse websocketResponse = new WebsocketResponse();
        websocketResponse.setMethod(WebsocketResponseMethod.NEW_MESSAGE);
        websocketResponse.setAdditionalData(om.writeValueAsString(newMessageData));

        sendWebsocketResponseToClient(websocketResponse, targetUserId);
    }

    @SneakyThrows
    private void sendWebsocketResponseToClient(WebsocketResponse websocketResponse, String targetUserId) {
        log.info("Send {} to {}", websocketResponse.getMethod().name(), targetUserId);

        List<Session> sessions = userSessionHashMap.get(targetUserId);
        if (sessions == null) {
            return;
        }

        Iterator<Session> i = sessions.iterator();
        while (i.hasNext()) {
            Session session = i.next();
            if (session.isOpen()) {
                session.getBasicRemote().sendText(om.writeValueAsString(websocketResponse));
            } else {
                i.remove();
            }
        }
    }

    private synchronized void addUserSessionToHashMap(Session session, String userId) {
        List<Session> sessions = userSessionHashMap.get(userId);
        if (sessions == null) {
            sessions = new ArrayList<>();
            sessions.add(session);
            userSessionHashMap.put(userId, sessions);
        } else {
            sessions.add(session);
        }
    }
}
