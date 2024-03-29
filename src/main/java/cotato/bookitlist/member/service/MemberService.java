package cotato.bookitlist.member.service;

import cotato.bookitlist.file.service.FileService;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.dto.MemberDto;
import cotato.bookitlist.member.dto.response.MemberRecommendListResponse;
import cotato.bookitlist.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Value("${recommend.count.member}")
    private int recommendCount;

    private static final String PROFILE_FILE_NAME = "profile";

    private final MemberRepository memberRepository;
    private final FileService fileService;

    public MemberDto getMemberInfo(Long memberId, Long loginMemberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        member.validatePublicProfile(loginMemberId);

        return MemberDto.from(member, loginMemberId);
    }

    @Transactional
    public String uploadProfile(MultipartFile profile, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        String url = fileService.uploadFileToS3(member.getId(), PROFILE_FILE_NAME, profile);

        return member.updateProfileLink(url);
    }

    @Transactional
    public void changeProfileStatus(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        member.changeProfileStatus();
    }

    @Transactional
    public void changeName(String name, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        member.changeName(name);
    }

    public MemberRecommendListResponse getNewMembers() {
        Pageable pageable = PageRequest.of(0, recommendCount, Sort.by("createdAt").descending());

        return MemberRecommendListResponse.of(memberRepository.findPublicMember(pageable));
    }
}
