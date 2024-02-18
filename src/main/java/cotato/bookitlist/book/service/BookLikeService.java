package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.domain.BookLike;
import cotato.bookitlist.book.dto.response.BookListResponse;
import cotato.bookitlist.book.repository.BookLikeRepository;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookLikeService {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final MemberRepository memberRepository;

    public Long registerLike(String isbn13, Long memberId) {
        if (bookLikeRepository.existsByBook_Isbn13AndMemberId(isbn13, memberId)) {
            throw new DuplicateKeyException("도서 좋아요가 이미 존재합니다.");
        }

        Book book = bookRepository.findByIsbn13(isbn13)
                .orElseGet(() -> bookRepository.getReferenceById(
                        bookService.registerBook(isbn13)
                ));

        Member member = memberRepository.getReferenceById(memberId);

        BookLike bookLike = BookLike.of(book, member);
        bookLike.increaseBookLikeCount();

        return bookLikeRepository.save(bookLike).getId();
    }

    public void deleteLike(String isbn13, Long memberId) {
        BookLike bookLike = bookLikeRepository.findByBook_Isbn13AndMemberId(isbn13, memberId)
                .orElseThrow(() -> new EntityNotFoundException("도서 좋아요 정보를 찾을 수 없습니다."));

        bookLike.decreaseBookLikeCount();

        bookLikeRepository.delete(bookLike);
    }

    public BookListResponse getLikeBooks(Long memberId, Pageable pageable) {
        return bookService.getLikeBooks(memberId, pageable);
    }

    public boolean likedBook(Long memberId, String isbn13) {
        return bookLikeRepository.existsByBook_Isbn13AndMemberId(isbn13, memberId);
    }
}
