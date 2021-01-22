package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.TokenApi;
import de.raphael.stellwag.generated.dto.TokenDto;
import de.raphael.stellwag.spring.meettogether.control.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class TokenApiImpl implements TokenApi {

    @Autowired
    private TokenService tokenService;

    @Override
    public ResponseEntity<TokenDto> createToken(String authorization) {
        TokenDto tokenDto = tokenService.createNewToken(authorization);
        return ResponseEntity.ok(tokenDto);
    }

    @Override
    public ResponseEntity<Void> verifyToken(String authorization) {
        return ResponseEntity.ok().build();
    }
}
