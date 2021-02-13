package de.raphael.stellwag.spring.meettogether.security.control;

import de.raphael.stellwag.spring.meettogether.entity.UserEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserRepository;
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
        UserDetails userDetailsRequest = getUserDetailsFromToken(authorization);
        UserDetails userDetailsDb = loadUserByUsername(userDetailsRequest.getUsername());
        return passwordEncoder.matches(userDetailsRequest.getPassword(), userDetailsDb.getPassword());
    }

    public UserDetails getUserDetailsFromToken(String authorization) {
        Base64.Decoder decoder = Base64.getDecoder();
        if (authorization.length()<7) {
            throw new RuntimeException("Not a Basic Authentification");
        }
        authorization = authorization.substring(6);
        byte [] test = decoder.decode(authorization);
        authorization = new String(test);

        if (!authorization.contains(":")) {
            throw new RuntimeException("Not a Basic Authentification");
        }

        String userId = authorization.substring(0,authorization.indexOf(":"));
        String password = authorization.substring(authorization.indexOf(":") + 1);
        return User.withUsername(userId).password(password).authorities("USER").build();
    }
}
