package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.UserApi;
import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.control.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.net.URI;

@Controller
public class UserApiImpl implements UserApi {

    private final UserService userService;

    @Autowired
    public UserApiImpl(UserService userService) {
        this.userService = userService;
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

    @Override
    public ResponseEntity<UserDto> loginUser(@Valid UserDto user, String authorization) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> registerUser(String userId, @Valid UserDto user, String authorization) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> renameUser(String userId, @Valid UserDto username, String authorization) {
        return null;
    }
}
