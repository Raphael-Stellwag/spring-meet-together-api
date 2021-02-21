package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.TokenDto;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import de.raphael.stellwag.spring.meettogether.security.control.MyUserDetailsService;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

    private final MyUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CurrentUser currentUser;


    @Autowired
    public TokenService(MyUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, CurrentUser currentUser) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.currentUser = currentUser;
    }

    public TokenDto createNewToken() {
        TokenDto tokenDto = new TokenDto();

        UserDetails userDetails = currentUser.getUserDetails();
        tokenDto.setToken(jwtTokenUtil.generateToken(userDetails));

        return tokenDto;
    }

}
