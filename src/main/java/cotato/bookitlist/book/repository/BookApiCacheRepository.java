package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.redis.BookApiCache;
import org.springframework.data.repository.CrudRepository;

public interface BookApiCacheRepository extends CrudRepository<BookApiCache, String> {
}
