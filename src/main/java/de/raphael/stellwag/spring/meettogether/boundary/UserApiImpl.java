package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.UserApi;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.control.UserService;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.net.URI;

@Controller
public class UserApiImpl implements UserApi {

    private final UserService userService;
    private final CurrentUser currentUser;

    @Autowired
    public UserApiImpl(UserService userService, JwtTokenUtil jwtTokenUtil, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @Override
    public ResponseEntity<UserDto> addUser(@Valid UserDto body) {
        UserDto userDto = userService.createNotRegisteredUser(body);
        return ResponseEntity.created(URI.create("/api/v1/user/" + userDto.getId())).body(userDto);
    }

    @Override
    public ResponseEntity<UserDto> loginUser(@Valid UserDto user) {
        String userId = userService.getUserId(user.getEmail(), user.getPassword());
        UserDto userDto = userService.getUserDto(userId);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> registerUser(@Valid UserDto user) {
        if (userService.doesEmailExist(user.getEmail())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.EMAIL_ALREADY_EXISTS);
        }
        user.setId(currentUser.getUserId());
        UserDto userDto = userService.registerUser(user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> renameUser(@Valid UserDto username) {
        log.debug("User {} wants to rename to {}", currentUser.getUserId(), username.getName());
        UserDto userDto = userService.renameUser(currentUser.getUserId(), username.getName());
        return ResponseEntity.ok(userDto);
    }
}
