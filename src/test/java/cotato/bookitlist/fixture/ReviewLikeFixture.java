package cotato.bookitlist.fixture;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.domain.entity.ReviewLike;
import org.springframework.test.util.ReflectionTestUtils;

public class ReviewLikeFixture {

    public static ReviewLike createReviewLike(Review review, Member member) {
        ReviewLike reviewLike = ReviewLike.of(member, review);
        ReflectionTestUtils.setField(reviewLike, "id", 1L);
        return reviewLike;
    }
}
