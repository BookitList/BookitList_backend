package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.domain.redis.BookApiCache;
import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.BookDto;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.dto.response.BookListResponse;
import cotato.bookitlist.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookApiComponent bookApiComponent;
    private final BookApiCacheService bookApiCacheService;
    private final BookRepository bookRepository;

    public BookApiListResponse searchExternal(String keyword, int start, int maxResults) {
        return bookApiComponent.findListByKeyWordAndApi(keyword, start, maxResults);
    }

    public BookApiDto getExternal(String isbn13) {
        return bookApiComponent.findByIsbn13(isbn13);
    }

    public BookDto getBookByIsbn13(String isbn13) {
        return bookRepository.findByIsbn13(isbn13).map(BookDto::from)
                .orElseGet(() -> bookApiCacheService.findBookApiCacheByIsbn13(isbn13)
                        .map(BookApiCache::getBookApiDto)
                        .map(BookDto::from)
                        .orElseGet(() -> BookDto.from(getExternal(isbn13)))
                );
    }

    @Deprecated
    public Page<Book> search(String keyword, Pageable pageable) {
        return bookRepository.findAllByKeyword(keyword, pageable);
    }

    public BookDto getBook(Long bookId) {
        return BookDto.from(bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다.")));
    }

    @Transactional
    public Long registerBook(String isbn13) {
        bookRepository.findByIsbn13(isbn13).ifPresent(book -> {
            throw new DuplicateKeyException("이미 등록된 isbn13입니다.");
        });

        return bookRepository.save(bookApiCacheService.findBookApiCacheByIsbn13(isbn13).map(BookApiCache::getBookApiDto)
                .orElseGet(() -> bookApiComponent.findByIsbn13(isbn13)).toEntity()
        ).getId();
    }


    public BookListResponse getLikeBooks(Long memberId, Pageable pageable) {
        return BookListResponse.from(bookRepository.findLikeBookByMemberId(memberId, pageable));
    }
}
