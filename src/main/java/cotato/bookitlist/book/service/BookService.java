package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.domain.redis.BookApiCache;
import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.BookDto;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookApiComponent bookApiComponent;
    private final BookApiCacheService bookApiCacheService;
    private final BookRepository bookRepository;

    public BookApiListResponse searchExternal(String keyword, int start) {
        return bookApiComponent.findListByKeyWordAndApi(keyword, start);
    }

    public BookApiDto findExternal(String isbn13) {
        return bookApiComponent.findByIsbn13(isbn13);
    }

    @Transactional(readOnly = true)
    public BookDto search(String isbn13) {
        return bookRepository.findByIsbn13(isbn13).map(BookDto::from)
                .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 isbn13입니다."));
    }

    @Transactional(readOnly = true)
    public Page<Book> search(String keyword, Pageable pageable) {
        return bookRepository.findAllByKeyword(keyword, pageable);
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
