package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.BookDto;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.dto.response.BookListResponse;
import cotato.bookitlist.book.dto.response.BookRecommendListResponse;
import cotato.bookitlist.book.dto.response.BookRecommendResponse;
import cotato.bookitlist.book.redis.BookApiCache;
import cotato.bookitlist.book.redis.RedissonLockService;
import cotato.bookitlist.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    @Value("${recommend.count.book}")
    private int recommendCount;

    private final BookApiComponent bookApiComponent;
    private final BookApiCacheService bookApiCacheService;
    private final BookRepository bookRepository;
    private final RedissonLockService redissonLockService;

    public BookApiListResponse searchExternal(String keyword, int start, int maxResults) {
        return bookApiComponent.findListByKeyWordAndApi(keyword, start, maxResults);
    }

    public BookApiDto getExternal(String isbn13) {
        return bookApiComponent.findByIsbn13(isbn13);
    }

    public BookDto getBookByIsbn13(String isbn13) {
        Optional<BookDto> book = getFromCache(isbn13);

        if (book.isPresent()) {
            return book.get();
        }

        try {
            if (redissonLockService.tryLock("book", isbn13)) {
                try {
                    book = getFromCache(isbn13);
                    if (book.isPresent()) {
                        return book.get();
                    }

                    BookDto bookDto = getFromDbOrExternal(isbn13);
                    bookApiCacheService.saveBookApiCache(BookApiDto.of(bookDto)); // 캐시에 저장
                    return bookDto;
                } finally {
                    redissonLockService.unlock("book", isbn13);
                }
            } else {
                log.debug("Lock not acquired, skipping cache update for isbn13={}", isbn13);
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while trying to lock", e);
        }
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

    public BookRecommendListResponse recommendBook() {
        List<BookRecommendResponse> bookRecommendList = bookRepository.findBooksByRandom(recommendCount).stream()
                .map(BookDto::from)
                .map(BookRecommendResponse::from)
                .toList();

        return new BookRecommendListResponse(bookRecommendList);
    }

    private Optional<BookDto> getFromCache(String isbn13) {
        return bookApiCacheService.findBookApiCacheByIsbn13(isbn13)
                .map(BookApiCache::getBookApiDto)
                .map(BookDto::from);
    }

    private BookDto getFromDbOrExternal(String isbn13) {
        return bookRepository.findByIsbn13(isbn13)
                .map(BookDto::from)
                .orElseGet(() -> BookDto.from(getExternal(isbn13)));
    }

}
