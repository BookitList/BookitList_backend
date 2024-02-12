package cotato.bookitlist.review.domain.entity;

import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private ReviewLike(Member member, Review review) {
        this.member = member;
        this.review = review;
    }

    public static ReviewLike of(Member member, Review review) {
        return new ReviewLike(member, review);
    }

    public void increaseReviewLikeCount() {
        review.increaseLikeCount();
    }

    public void decreaseReviewLikeCount() {
        review.decreaseLikeCount();
    }
}
