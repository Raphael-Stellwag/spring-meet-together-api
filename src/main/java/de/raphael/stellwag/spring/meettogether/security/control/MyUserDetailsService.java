package de.raphael.stellwag.spring.meettogether.security.control;

import de.raphael.stellwag.spring.meettogether.entity.dao.UserRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.UserEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads the UserDetails from the database for the given User
     *
     * @param username normally from jwt or basic Auth
     * @return UserDetails with Data from database
     * @throws UsernameNotFoundException User does not exist in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Got called");

        Optional<UserEntity> optionalUserEntity = userRepository.findById(username);

        if (optionalUserEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found in database");
        }
        UserEntity user = optionalUserEntity.get();

        return User.withUsername(user.getId()).password(user.getPassword()).authorities("USER").build();
    }

    public boolean checkBasicAuth(String authorization) {
        log.info("before userDetailsRequest");
        UserDetails userDetailsRequest = getUserDetailsFromBasicAuth(authorization);
        log.info("after userDetailsRequest , {}", userDetailsRequest);
        log.info("after userDetailsRequest Password , {}", userDetailsRequest.getPassword());
        UserDetails userDetailsDb = loadUserByUsername(userDetailsRequest.getUsername());
        log.info("after userDetailsDb, {}", userDetailsDb);
        log.info("after userDetailsDb Password, {}", userDetailsDb.getPassword());
        log.info("equals: {}", passwordEncoder.matches(userDetailsRequest.getPassword(), userDetailsDb.getPassword()));
        return passwordEncoder.matches(userDetailsRequest.getPassword(), userDetailsDb.getPassword());
    }

    public UserDetails getUserDetailsFromBasicAuth(String authorization) {

        if (authorization.length() < 7) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_AUTHORIZED);
        }

        authorization = authorization.substring(6);

        byte[] base64BasicAuth;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            base64BasicAuth = decoder.decode(authorization);
        } catch (IllegalArgumentException e) {
            log.warn("Could not decode a basic auth string", e);
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_AUTHORIZED);
        }

        authorization = new String(base64BasicAuth);

        if (!authorization.contains(":")) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_AUTHORIZED);
        }

        String userId = authorization.substring(0, authorization.indexOf(":"));
        String password = authorization.substring(authorization.indexOf(":") + 1);
        return User.withUsername(userId).password(password).authorities("USER").build();
    }
}
