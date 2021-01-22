package de.raphael.stellwag.spring.meettogether.error;

import lombok.Getter;

@Getter
public class MeetTogetherException extends RuntimeException {
    private final MeetTogetherExceptionEnum meetTogetherExceptionEnum;

    public MeetTogetherException(MeetTogetherExceptionEnum meetTogetherExceptionEnum) {
        this.meetTogetherExceptionEnum = meetTogetherExceptionEnum;
    }
}
