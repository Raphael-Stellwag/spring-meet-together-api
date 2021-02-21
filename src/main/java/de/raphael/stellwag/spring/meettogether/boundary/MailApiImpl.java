package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.MailApi;
import de.raphael.stellwag.generated.dto.MailDto;
import de.raphael.stellwag.generated.dto.MessageDto;
import de.raphael.stellwag.spring.meettogether.control.MailService;
import de.raphael.stellwag.spring.meettogether.control.UserInEventService;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class MailApiImpl implements MailApi {

    private final MailService mailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserInEventService userInEventService;
    private final CurrentUser currentUser;

    @Autowired
    public MailApiImpl(MailService mailService, JwtTokenUtil jwtTokenUtil, UserInEventService userInEventService, CurrentUser currentUser) {
        this.mailService = mailService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userInEventService = userInEventService;
        this.currentUser = currentUser;
    }

    @Override
    public ResponseEntity<MessageDto> sendMail(String eventId, @Valid MailDto mailBody) {
        String userId = currentUser.getUserName();

        if (!userInEventService.isUserInEvent(userId, eventId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_IN_EVENT);
        }

        mailService.sendSimpleMessage(mailBody.getRecipients().toArray(new String[mailBody.getRecipients().size()]),
                mailBody.getSubject(), mailBody.getHtmlContent());

        return null;
    }
}
