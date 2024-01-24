package cotato.bookitlist.auth.service;

import cotato.bookitlist.auth.domain.BlackList;
import cotato.bookitlist.auth.domain.RefreshTokenEntity;
import cotato.bookitlist.auth.dto.request.LogoutRequest;
import cotato.bookitlist.auth.dto.request.ReissueRequest;
import cotato.bookitlist.auth.dto.response.ReissueResponse;
import cotato.bookitlist.auth.repository.BlackListRepository;
import cotato.bookitlist.auth.repository.RefreshTokenRepository;
import cotato.bookitlist.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

    @Value("${auth.jwt.refreshExp}")
    private Long refreshExp;

    public ReissueResponse tokenReissue(ReissueRequest reissueRequest) {
        RefreshTokenEntity savedRefreshTokenEntity =
                refreshTokenRepository.findByRefreshToken(reissueRequest.refreshToken()).orElseThrow();
        Long refreshMemberId = jwtTokenProvider.parseRefreshToken(savedRefreshTokenEntity.getRefreshToken());
        String newAccessToken = jwtTokenProvider.generateAccessToken(refreshMemberId, "USER"); // TODO: Role 추가해야 함!!
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(refreshMemberId);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(refreshMemberId)
                .refreshToken(newRefreshToken)
                .ttl(refreshExp)
                .build();
        saveRefreshToken(refreshTokenEntity);
        return ReissueResponse.from(newAccessToken, refreshTokenEntity.getRefreshToken());
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
}
