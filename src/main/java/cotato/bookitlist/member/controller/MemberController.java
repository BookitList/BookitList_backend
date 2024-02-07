package cotato.bookitlist.member.controller;

import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.member.dto.ProfileResponse;
import cotato.bookitlist.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/profiles")
    public ResponseEntity<ProfileResponse> uploadProfile(
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal AuthDetails details
    ) {
        return ResponseEntity.ok(ProfileResponse.of(memberService.uploadProfile(multipartFile, details.getId())));
    }
}
