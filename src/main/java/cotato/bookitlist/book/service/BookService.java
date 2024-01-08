package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.redis.BookApiCache;
import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookApiComponent bookApiComponent;
    private final BookApiCacheService bookApiCacheService;
    private final BookRepository bookRepository;

    public BookApiResponse searchExternal(String keyWord, int start) {
        return bookApiComponent.findListByKeyWordAndApi(keyWord, start);
    }

    public BookApiDto searchExternal(String isbn13) {
        return bookApiComponent.findByIsbn13(isbn13);
    }

    public Long registerBook(String isbn13) {
        bookRepository.findByIsbn13(isbn13).ifPresent(book -> {
            throw new DuplicateKeyException("이미 등록된 isbn13입니다.");
        });

        return bookRepository.save(bookApiCacheService.findBookApiCacheByIsbn13(isbn13).map(BookApiCache::getBookApiDto)
                .orElseGet(() -> bookApiComponent.findByIsbn13(isbn13)).toEntity()
        ).getId();
    }
}
