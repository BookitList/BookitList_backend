package cotato.bookitlist.auth.service;

import cotato.bookitlist.auth.domain.BlackList;
import cotato.bookitlist.auth.domain.RefreshTokenEntity;
import cotato.bookitlist.auth.dto.request.LogoutRequest;
import cotato.bookitlist.auth.dto.request.ReissueRequest;
import cotato.bookitlist.auth.dto.response.ReissueResponse;
import cotato.bookitlist.auth.repository.BlackListRepository;
import cotato.bookitlist.auth.repository.RefreshTokenRepository;
import cotato.bookitlist.config.security.jwt.JwtTokenProvider;
import cotato.bookitlist.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final MemberRepository memberRepository;

    @Value("${auth.jwt.refreshExp}")
    private Long refreshExp;

    public ReissueResponse tokenReissue(ReissueRequest reissueRequest) {

        Long refreshMemberId = jwtTokenProvider.parseRefreshToken(reissueRequest.refreshToken());

        if (refreshTokenRepository.findByRefreshToken(reissueRequest.refreshToken()).isEmpty()) {
            // Refresh Token 탈취 가능성 존재
            deleteRefreshTokenOfMember(refreshMemberId);
            throw new IllegalArgumentException("잘못된 Refresh Token 입니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(refreshMemberId, "USER");
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(refreshMemberId);

        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .id(refreshMemberId)
                .refreshToken(newRefreshToken)
                .ttl(refreshExp)
                .build();

        saveRefreshToken(newRefreshTokenEntity);
        return ReissueResponse.from(newAccessToken, newRefreshTokenEntity.getRefreshToken());
    }

    private void deleteRefreshTokenOfMember(Long memberId) {
        refreshTokenRepository.findById(memberId).ifPresent(refreshTokenRepository::delete);
    }

    public RefreshTokenEntity saveRefreshToken(Long id, String refreshToken) {

        return refreshTokenRepository.save(RefreshTokenEntity.builder()
                .id(id)
                .refreshToken(refreshToken)
                .ttl(refreshExp)
                .build());
    }

    public RefreshTokenEntity saveRefreshToken(RefreshTokenEntity refreshTokenEntity) {

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public void logout(LogoutRequest logoutRequest) {
        setBlackList(logoutRequest.accessToken());
        deleteRefreshToken(logoutRequest.refreshToken());
    }

    private void setBlackList(String accessToken) {
        blackListRepository.save(BlackList.builder()
                .id(accessToken)
                .ttl(jwtTokenProvider.getExpiration(accessToken))
                .build());
    }

    private void deleteRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow();
        refreshTokenRepository.delete(refreshTokenEntity);
    }

    public boolean isBlocked(String accessToken) {
        return blackListRepository.findById(accessToken).isPresent();
    }

    @Transactional(readOnly = true)
    public void validateRegisteredMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException("유저 정보를 찾을 수 없습니다.");
        }
    }
}
