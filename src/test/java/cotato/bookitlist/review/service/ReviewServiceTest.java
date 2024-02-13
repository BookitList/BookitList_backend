package cotato.bookitlist.review.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static cotato.bookitlist.review.domain.ReviewStatus.PUBLIC;
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

    private Review createReview(Long reviewId) {
        Review review = Review.of(createMember(), createBook(), "content", PUBLIC);
        ReflectionTestUtils.setField(review, "id", reviewId);
        return review;
    }

    private Book createBook() {
        return Book.of("title", "author", "publisher", LocalDate.now(), "description", "link", "isbn13", 10000, "cover");
    }

    Member createMember(Long memberId) {
        Member member = new Member("email", "name", "oauth2Id", AuthProvider.KAKAO, "profile");
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    private Member createMember() {
        return createMember(1L);
    }
}
