package cotato.bookitlist.auth.controller;

import cotato.bookitlist.auth.dto.AccessTokenResponse;
import cotato.bookitlist.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health_check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/token/reissue")
    public ResponseEntity<AccessTokenResponse> tokenRefresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.tokenReissue(refreshToken));
    }

}
