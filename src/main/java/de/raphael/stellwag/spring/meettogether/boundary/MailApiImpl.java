package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.MailApi;
import de.raphael.stellwag.generated.dto.MailDto;
import de.raphael.stellwag.generated.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class MailApiImpl implements MailApi {

    @Override
    public ResponseEntity<MessageDto> sendMail(String authorization, String eventId, @Valid MailDto mailBody) {
        return null;
    }
}
