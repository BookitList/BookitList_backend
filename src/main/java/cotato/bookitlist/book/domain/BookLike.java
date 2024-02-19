package cotato.bookitlist.book.domain;

import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private BookLike(Book book, Member member) {
        this.book = book;
        this.member = member;
    }

    public static BookLike of(Book book, Member member ) {
        return new BookLike(book, member);
    }

    public void increaseBookLikeCount() {
        book.increaseLikeCount();
    }

    public void decreaseBookLikeCount() {
        book.decreaseLikeCount();
    }
}
