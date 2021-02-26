package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.TokenDto;
import de.raphael.stellwag.spring.meettogether.helpers.CurrentUser;
import de.raphael.stellwag.spring.meettogether.security.control.MyUserDetailsService;
import de.raphael.stellwag.spring.meettogether.security.helpers.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class TokenService {

    private final JwtTokenUtil jwtTokenUtil;
    private final CurrentUser currentUser;


    @Autowired
    public TokenService(MyUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, CurrentUser currentUser) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.currentUser = currentUser;
    }

    public TokenDto createNewToken() {
        TokenDto tokenDto = new TokenDto();

        UserDetails userDetails = currentUser.getUserDetails();

        String token = jwtTokenUtil.generateToken(userDetails);
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        tokenDto.setToken(token);
        tokenDto.setExpirationDate(expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        return tokenDto;
    }

}
