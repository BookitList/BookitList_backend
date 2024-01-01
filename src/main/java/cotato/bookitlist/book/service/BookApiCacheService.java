package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.redis.BookApiCache;
import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.repository.BookApiCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookApiCacheService {

    private final BookApiCacheRepository repository;

    public void saveBookApiCache(BookApiDto bookApiDto) {
        repository.save(new BookApiCache(bookApiDto.isbn13(), bookApiDto));
    }

    public Optional<BookApiCache> findBookApiCache(String isbn13){
        return repository.findById(isbn13);
    }

    public void deleteBookApiCache(String isbn13) {
        repository.findById(isbn13)
                .ifPresent(repository::delete);
    }
}
