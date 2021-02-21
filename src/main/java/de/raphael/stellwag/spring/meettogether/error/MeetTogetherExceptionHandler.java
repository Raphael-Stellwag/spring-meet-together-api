package de.raphael.stellwag.spring.meettogether.error;

import de.raphael.stellwag.generated.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class MeetTogetherExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MeetTogetherException.class)
    public final ResponseEntity<Object> handleMeetTogetherException(MeetTogetherException ex, WebRequest request) {

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(ex.getMeetTogetherExceptionEnum().getMessage());
        apiResponseDto.setStatus(ex.getMeetTogetherExceptionEnum().getHttpStatus().value());
        apiResponseDto.setError(ex.getMeetTogetherExceptionEnum().name());
        apiResponseDto.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(ex.getMessage());
        apiResponseDto.setError("UERNAME_NOT_FOUND");

        apiResponseDto.setTimestamp(LocalDateTime.now());
        apiResponseDto.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(apiResponseDto.getStatus()).body(apiResponseDto);
    }

}
