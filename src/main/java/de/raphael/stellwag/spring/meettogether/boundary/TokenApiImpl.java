package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.generated.api.TokenApi;
import de.raphael.stellwag.generated.dto.TokenDto;
import de.raphael.stellwag.spring.meettogether.control.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class TokenApiImpl implements TokenApi {

    private TokenService tokenService;

    @Autowired
    public TokenApiImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity<TokenDto> createToken() {
        TokenDto tokenDto = tokenService.createNewToken();
        return ResponseEntity.ok(tokenDto);
    }

    @Override
    public ResponseEntity<Void> verifyToken() {
        return ResponseEntity.ok().build();
    }
}
