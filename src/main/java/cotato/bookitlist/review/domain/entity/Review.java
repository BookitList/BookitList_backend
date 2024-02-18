package cotato.bookitlist.review.domain.entity;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.review.domain.ReviewStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.access.AccessDeniedException;

@Getter
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    private int likeCount = 0;

    private int viewCount = 0;

    private boolean deleted = false;

    private Review(Member member, Book book, String content, ReviewStatus status) {
        this.member = member;
        this.book = book;
        this.content = content;
        this.status = status;
    }

    public static Review of(Member member, Book book, String content, ReviewStatus status) {
        return new Review(member, book, content, status);
    }

    public void updateReview(Member member, String content, ReviewStatus status) {
        if (!this.member.getId().equals(member.getId())) {
            throw new AccessDeniedException("권한이 없는 유저입니다.");
        }

        this.content = content;
        this.status = status;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    // 좋아요는 모두 삭제하고 조회수 등 나머지 내용은 유지한다.
    public void deleteReview() {
        deleted = true;
        likeCount = 0;
    }
}
