package cotato.bookitlist.member.controller;

import cotato.bookitlist.security.jwt.AuthDetails;
import cotato.bookitlist.member.dto.request.NameChangeRequest;
import cotato.bookitlist.member.dto.response.MemberRecommendListResponse;
import cotato.bookitlist.member.dto.response.MemberResponse;
import cotato.bookitlist.member.dto.response.ProfileResponse;
import cotato.bookitlist.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private static final Long DEFAULT_USER_ID = 0L;

    private final MemberService memberService;

    @GetMapping("/{member-id}")
    public ResponseEntity<MemberResponse> getMemberInfo(
            @PathVariable("member-id") Long memberId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(MemberResponse.from(memberService.getMemberInfo(memberId, DEFAULT_USER_ID)));
        }
        return ResponseEntity.ok(MemberResponse.from(memberService.getMemberInfo(memberId, details.getId())));
    }

    @PatchMapping("/profiles")
    public ResponseEntity<ProfileResponse> uploadProfile(
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal AuthDetails details
    ) {
        return ResponseEntity.ok(ProfileResponse.of(memberService.uploadProfile(multipartFile, details.getId())));
    }

    @PatchMapping("/profile-status")
    public ResponseEntity<Void> changeProfileStatus(
            @AuthenticationPrincipal AuthDetails details
    ) {
        memberService.changeProfileStatus(details.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> changeName(
            @Valid @RequestBody NameChangeRequest nameChangeRequest,
            @AuthenticationPrincipal AuthDetails details
    ) {
        memberService.changeName(nameChangeRequest.name(), details.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public ResponseEntity<MemberRecommendListResponse> getNewMembers() {
        return ResponseEntity.ok(memberService.getNewMembers());
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(
            @AuthenticationPrincipal AuthDetails details
    ) {
        return ResponseEntity.ok(MemberResponse.from(memberService.getMemberInfo(details.getId(), details.getId())));
    }
}
