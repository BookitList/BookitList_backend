package cotato.bookitlist.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshTokenEntity {

    @Id
    private Long id;

    @Indexed
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS) // TTL
    private Long ttl;

    @Builder
    public RefreshTokenEntity(Long id, String refreshToken, Long ttl) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.ttl = ttl;
    }

    public void updateTTL(Long ttl) {
        this.ttl += ttl;
    }
}
