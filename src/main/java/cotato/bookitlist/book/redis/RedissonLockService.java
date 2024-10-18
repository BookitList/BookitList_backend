package cotato.bookitlist.book.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockService {
    private static final String LOCK_PREFIX = "LOCK";
    private final RedissonClient redissonClient;

    private static String getLockKey(String domain, String key) {
        return LOCK_PREFIX + ":" + domain + ":" + key;
    }

    public boolean tryLock(String domain, Object key) throws InterruptedException {
        RLock lock = redissonClient.getLock(getLockKey(domain, key.toString()));
        log.debug("tryLock domain={} key={}", domain, key);
        return lock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
    }

    public void unlock(String domain, Object key) {
        RLock lock = redissonClient.getLock(getLockKey(domain, key.toString()));
        log.debug("unlock domain={} key={}", domain, key);
        lock.unlock();
    }
}
