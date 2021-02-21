package de.raphael.stellwag.spring.meettogether.error;

import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class MeetTogetherException extends ResponseStatusException {
    private final MeetTogetherExceptionEnum meetTogetherExceptionEnum;

    public MeetTogetherException(MeetTogetherExceptionEnum meetTogetherExceptionEnum) {
        super(meetTogetherExceptionEnum.getHttpStatus(), meetTogetherExceptionEnum.getMessage());
        this.meetTogetherExceptionEnum = meetTogetherExceptionEnum;
    }
}
