package cotato.bookitlist.mark.domain;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarkBook {

    @Id
    @GeneratedValue
    @Column(name = "like_book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
