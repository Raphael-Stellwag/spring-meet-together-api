package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.TokenDto;
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


    @Autowired
    public TokenService(MyUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public TokenDto createNewToken(String authorization) {
        TokenDto tokenDto = new TokenDto();

        UserDetails userDetails = userDetailsService.getUserDetailsFromToken(authorization);
        tokenDto.setToken(jwtTokenUtil.generateToken(userDetails));

        return tokenDto;
    }

}
