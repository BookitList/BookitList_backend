package cotato.bookitlist.fixture;

import cotato.bookitlist.review.domain.ReviewStatus;
import cotato.bookitlist.review.domain.entity.Review;
import org.springframework.test.util.ReflectionTestUtils;

import static cotato.bookitlist.fixture.BookFixture.createBook;
import static cotato.bookitlist.fixture.MemberFixture.createMember;

public class ReviewFixture {
    public static Review createReview(Long reviewId, Long memberId) {
        Review review = Review.of(createMember(), createBook(), "content", ReviewStatus.PUBLIC);
        ReflectionTestUtils.setField(review, "id", reviewId);
        return review;
    }

    public static Review createReview(Long reviewId) {
        return createReview(reviewId, 1L);
    }
}
