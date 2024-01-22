package cotato.bookitlist.auth.service;

import cotato.bookitlist.auth.domain.RefreshTokenEntity;
import cotato.bookitlist.auth.dto.ReissueRequest;
import cotato.bookitlist.auth.dto.ReissueResponse;
import cotato.bookitlist.auth.repository.RefreshTokenRepository;
import cotato.bookitlist.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${auth.jwt.refreshExp}")
    private Long refreshExp;

    @Transactional(readOnly = true)
    public ReissueResponse tokenReissue(ReissueRequest reissueRequest) {
        RefreshTokenEntity savedRefreshTokenEntity =
                refreshTokenRepository.findByRefreshToken(reissueRequest.refreshToken()).orElseThrow();
        Long refreshMemberId = jwtTokenProvider.parseRefreshToken(savedRefreshTokenEntity.getRefreshToken());
        String newAccessToken = jwtTokenProvider.generateAccessToken(refreshMemberId, "USER"); // TODO: Role 추가해야 함!!
        return ReissueResponse.of(newAccessToken);
    }

    @Transactional
    public RefreshTokenEntity saveRefreshToken(Long memberId) {

        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        return refreshTokenRepository.save(RefreshTokenEntity.builder()
                .id(memberId)
                .refreshToken(refreshToken)
                .ttl(refreshExp)
                .build());
    }

}
