package cotato.bookitlist.auth.controller;

import cotato.bookitlist.auth.dto.ReissueResponse;
import cotato.bookitlist.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health_check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/token/reissue")
    public ResponseEntity<ReissueResponse> tokenRefresh(@RequestBody ReissueRequest reissueRequest) {
        return ResponseEntity.ok(authService.tokenReissue(reissueRequest));
    }

}
