package cotato.bookitlist.mark.domain;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeReview {

    @Id
    @GeneratedValue
    @Column(name = "like_review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
