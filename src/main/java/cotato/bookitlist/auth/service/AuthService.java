package cotato.bookitlist.auth.service;

import cotato.bookitlist.auth.domain.BlackList;
import cotato.bookitlist.auth.domain.RefreshTokenEntity;
import cotato.bookitlist.auth.dto.LogoutRequest;
import cotato.bookitlist.auth.dto.ReissueRequest;
import cotato.bookitlist.auth.dto.ReissueResponse;
import cotato.bookitlist.auth.repository.BlackListRepository;
import cotato.bookitlist.auth.repository.RefreshTokenRepository;
import cotato.bookitlist.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

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

    @Transactional
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
        Optional<BlackList> blackList = blackListRepository.findById(accessToken);
        return blackList.isPresent();
    }
}
