package cotato.bookitlist.book.service;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.domain.BookLike;
import cotato.bookitlist.book.repository.BookLikeRepository;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static cotato.bookitlist.fixture.BookFixture.createBook;
import static cotato.bookitlist.fixture.BookLikeFixture.createBookLike;
import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("도서 좋아요 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class BookLikeServiceTest {

    @InjectMocks
    BookLikeService sut;
    @Mock
    BookService bookService;
    @Mock
    BookRepository bookRepository;
    @Mock
    BookLikeRepository bookLikeRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("도서 좋아요를 생성시 게시글 likeCount가 증가한다.")
    void givenIsbn13_whenRegisteringBookLike_thenRegisterBookLike() throws Exception {
        //given
        String isbn13 = "9788931514810";
        Member member = createMember();
        Book book = createBook(isbn13);

        given(bookLikeRepository.existsByBook_Isbn13AndMemberId(isbn13, member.getId())).willReturn(false);
        given(bookRepository.findByIsbn13(isbn13)).willReturn(Optional.of(book));
        given(memberRepository.getReferenceById(member.getId())).willReturn(member);
        given(bookLikeRepository.save(any(BookLike.class))).willReturn(createBookLike(book, member));

        //when
        sut.registerLike(isbn13, member.getId());

        //then
        then(bookLikeRepository).should().existsByBook_Isbn13AndMemberId(isbn13, member.getId());
        then(bookRepository).should().findByIsbn13(isbn13);
        then(memberRepository).should().getReferenceById(member.getId());
        then(bookLikeRepository).should().save(any(BookLike.class));
        assertThat(book.getLikeCount()).isOne();
    }

    @Test
    @DisplayName("DB에 존재하지 않는 도서 좋아요를 생성시 게시글 likeCount가 증가한다.")
    void givenIsbn13NonExistedInDB_whenRegisteringBookLike_thenRegisterBookLike() throws Exception {
        //given
        String isbn13 = "9788966262281";
        Member member = createMember();
        Book book = createBook(isbn13);

        given(bookLikeRepository.existsByBook_Isbn13AndMemberId(isbn13, member.getId())).willReturn(false);
        given(bookRepository.findByIsbn13(isbn13)).willReturn(Optional.empty());
        given(bookService.registerBook(isbn13)).willReturn(book.getId());
        given(bookRepository.getReferenceById(book.getId())).willReturn(book);
        given(memberRepository.getReferenceById(member.getId())).willReturn(member);
        given(bookLikeRepository.save(any(BookLike.class))).willReturn(createBookLike(book, member));

        //when
        sut.registerLike(isbn13, member.getId());

        //then
        then(bookLikeRepository).should().existsByBook_Isbn13AndMemberId(isbn13, member.getId());
        then(bookRepository).should().findByIsbn13(isbn13);
        then(bookService).should().registerBook(isbn13);
        then(bookRepository).should().getReferenceById(book.getId());
        then(memberRepository).should().getReferenceById(member.getId());
        then(bookLikeRepository).should().save(any(BookLike.class));
        assertThat(book.getLikeCount()).isOne();
    }

    @Test
    @DisplayName("도서 좋아요 삭제시 LikeCount가 감소한다.")
    void givenIsbn13_whenDeletingBookLike_thenDeleteBookLike() throws Exception {
        //given
        String isbn13 = "9788931514810";
        Member member = createMember();
        Book book = createBook(isbn13);
        BookLike bookLike = createBookLike(book, member);
        book.increaseLikeCount();

        given(bookLikeRepository.findByBook_Isbn13AndMemberId(isbn13, member.getId())).willReturn(Optional.of(bookLike));
        willDoNothing().given(bookLikeRepository).delete(bookLike);

        //when
        sut.deleteLike(isbn13, member.getId());

        //then
        then(bookLikeRepository).should().findByBook_Isbn13AndMemberId(isbn13, member.getId());
        then(bookLikeRepository).should().delete(bookLike);
        assertThat(book.getLikeCount()).isZero();
    }

}
