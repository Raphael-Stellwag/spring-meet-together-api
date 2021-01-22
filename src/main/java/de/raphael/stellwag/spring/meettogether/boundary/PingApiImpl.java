package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.PingApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class PingApiImpl implements PingApi {

    @Override
    public ResponseEntity<Void> ping() {
        log.debug("Ping was called");
        return ResponseEntity.ok().build();
    }
}
