package cotato.bookitlist.review.service;

import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cotato.bookitlist.fixture.ReviewFixture.createReview;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("한줄요약 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService sut;
    @Mock
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("한줄요약 조회수 증가 요청 시 조회수가 증가한다.")
    void givenReviewId_whenIncreasingViewCount_thenIncreaseViewCount() {
        //given
        Long reviewId = 1L;
        Review review = createReview(reviewId);
        given(reviewRepository.getReferenceById(reviewId)).willReturn(review);

        //when
        sut.increaseViewCount(reviewId);

        //then
        then(reviewRepository).should().getReferenceById(reviewId);
        assertThat(review.getViewCount()).isEqualTo(1);
    }

}
