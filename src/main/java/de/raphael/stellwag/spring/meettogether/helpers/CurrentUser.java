package de.raphael.stellwag.spring.meettogether.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CurrentUser {

    public String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userPrincipal = (UserDetails) auth.getPrincipal();
            log.info("User principal name = {}", userPrincipal.getUsername());
            return userPrincipal.getUsername();
        }
        log.warn("No User Principal");
        return null;
    }

    public UserDetails getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (UserDetails) auth.getPrincipal();
        }
        log.warn("No User Principal");
        return null;
    }
}
