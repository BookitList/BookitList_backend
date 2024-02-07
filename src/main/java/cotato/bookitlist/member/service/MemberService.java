package cotato.bookitlist.member.service;

import cotato.bookitlist.file.service.FileService;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private static final String PROFILE_FILE_NAME = "profile";

    private final MemberRepository memberRepository;
    private final FileService fileService;

    public String uploadProfile(MultipartFile profile, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        String url = fileService.uploadFileToS3(member.getId(), PROFILE_FILE_NAME, profile);

        return member.updateProfileLine(url);
    }
}
