package cotato.bookitlist.auth.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash(value = "blackList")
public class BlackList {

    @Id
    String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS) //TTL
    private Long ttl;

}
