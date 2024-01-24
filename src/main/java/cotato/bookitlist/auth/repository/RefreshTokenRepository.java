package cotato.bookitlist.auth.repository;

import cotato.bookitlist.auth.domain.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
