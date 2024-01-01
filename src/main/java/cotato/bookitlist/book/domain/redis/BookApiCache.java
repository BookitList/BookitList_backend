package cotato.bookitlist.book.domain.redis;

import cotato.bookitlist.book.dto.BookApiDto;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "bookApiCache", timeToLive = 1800)
@AllArgsConstructor
public class BookApiCache {
    @Id
    private String id;

    private BookApiDto bookApiDto;
}
