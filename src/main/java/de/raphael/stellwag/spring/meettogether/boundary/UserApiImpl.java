package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.UserApi;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.control.UserService;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.security.control.MyUserDetailsService;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.net.URI;

@Controller
public class UserApiImpl implements UserApi {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MyUserDetailsService userDetailsService;

    @Autowired
    public UserApiImpl(UserService userService, JwtTokenUtil jwtTokenUtil, MyUserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public ResponseEntity<UserDto> addUser(@Valid UserDto body) {
        UserDto userDto = userService.createNotRegisteredUser(body);
        return ResponseEntity.created(URI.create("/api/v1/user/" + userDto.getId())).body(userDto);
    }

    //TODO: check for what this is used
    @Override
    public ResponseEntity<UserDto> getUserById(String userId, String authorization) {
        return null;
    }

    //TODO delete authorization here
    @Override
    public ResponseEntity<UserDto> loginUser(@Valid UserDto user, String authorization) {
        String userId = userService.getUserId(user.getEmail(), user.getPassword());
        UserDto userDto = userService.getUserDto(userId);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> registerUser(String userId, @Valid UserDto user, String authorization) {
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        if (userService.doesEmailExist(user.getEmail())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.EMAIL_ALREADY_EXISTS);
        }
        user.setId(userId);
        UserDto userDto = userService.registerUser(user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> renameUser(String userId, @Valid UserDto username, String authorization) {
        log.debug("User {} wants to rename to {}", userId, username.getName());
        if (!jwtTokenUtil.headerBelongsToUser(authorization, userId)) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.NOT_ALLOWED);
        }
        UserDto userDto = userService.renameUser(userId, username.getName());
        return ResponseEntity.ok(userDto);
    }
}
