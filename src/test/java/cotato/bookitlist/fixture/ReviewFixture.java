package cotato.bookitlist.fixture;

import cotato.bookitlist.review.domain.entity.Review;
import org.springframework.test.util.ReflectionTestUtils;

import static cotato.bookitlist.fixture.BookFixture.createBook;
import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static cotato.bookitlist.review.domain.ReviewStatus.PUBLIC;

public class ReviewFixture {
    public static Review createReview(Long reviewId) {
        Review review = Review.of(createMember(), createBook(), "content", PUBLIC);
        ReflectionTestUtils.setField(review, "id", reviewId);
        return review;
    }
}
