package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.domain.entity.BookLike;
import cotato.bookitlist.book.repository.BookLikeRepository;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
        Book book = bookRepository.findByIsbn13(isbn13)
                .orElseGet(() -> bookRepository.getReferenceById(
                        bookService.registerBook(isbn13)
                ));

        Member member = memberRepository.getReferenceById(memberId);

        BookLike bookLike = BookLike.of(book, member);
        bookLike.increaseBookLikeCount();

        return bookLikeRepository.save(bookLike).getId();
    }
}
