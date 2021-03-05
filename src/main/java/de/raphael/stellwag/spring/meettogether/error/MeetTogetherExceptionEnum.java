package de.raphael.stellwag.spring.meettogether.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MeetTogetherExceptionEnum {
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "The password was not correct"),
    EMAIL_DOES_NOT_EXIST(HttpStatus.FORBIDDEN, "This email address does not exist"),
    NOT_ALLOWED(HttpStatus.FORBIDDEN, "This Operation was forbidden"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "This User does not exist"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "This Event does not exist"),
    USER_NOT_IN_EVENT(HttpStatus.FORBIDDEN, "This user is not in the event"),
    NO_JWT_IN_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "You need to add the JWT Token in the Header"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "An account with this email adress already exists"),
    TIME_PLACE_SUGGESTION_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "This TimePlace Suggestion does not exist"),
    USER_ALREADY_IN_TIME_PLACE_SUGGESTION(HttpStatus.CONFLICT, "User already in TimePlace Suggestion"),
    USER_NOT_IN_TIME_PLACE_SUGGESTION(HttpStatus.CONFLICT, "User is not in TimePlace Suggestion"),
    USER_NOT_CREATOR_OF_EVENT(HttpStatus.FORBIDDEN, "You are not the creator of the event"),
    TIME_PLACE_SUGGESTION_BELONGS_NOT_TO_EVENT(HttpStatus.CONFLICT, "TimePlace Suggestion belongs not to this event"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "The provided token is expired"),
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "You didn't provide a valid authorization header");

    private final HttpStatus httpStatus;
    private final String message;

}
