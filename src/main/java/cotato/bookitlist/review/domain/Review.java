package cotato.bookitlist.review.domain;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE review_id = ?")
@SQLRestriction("deleted = false")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memer_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private String content;

    private int likeCount = 0;

    private boolean deleted = false;
}
