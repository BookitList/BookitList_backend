package cotato.bookitlist.review.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.domain.entity.ReviewLike;
import cotato.bookitlist.review.repository.ReviewLikeRepository;
import cotato.bookitlist.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static cotato.bookitlist.fixture.ReviewFixture.createReview;
import static cotato.bookitlist.fixture.ReviewLikeFixture.createReviewLike;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("한줄요약 좋아요 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ReviewLikeServiceTest {

    @InjectMocks
    ReviewLikeService sut;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ReviewLikeRepository reviewLikeRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("한즐요약 좋아요 생성 시 한줄요약 likeCount가 증가한다.")
    void givenReviewId_whenRegisteringReviewLike_thenRegisterReviewLike() {
        //given
        Long reviewId = 1L;
        Long memberId = 1L;
        Review review = createReview(reviewId);
        Member member = createMember(memberId);

        given(reviewLikeRepository.existsByReviewIdAndMemberId(reviewId, memberId)).willReturn(false);
        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
        given(memberRepository.getReferenceById(anyLong())).willReturn(member);
        given(reviewLikeRepository.save(any(ReviewLike.class))).willReturn(createReviewLike(review, member));

        //when
        sut.registerLike(reviewId, memberId);

        //then
        then(reviewLikeRepository).should().existsByReviewIdAndMemberId(reviewId, memberId);
        then(reviewRepository).should().findById(anyLong());
        then(memberRepository).should().getReferenceById(anyLong());
        then(reviewLikeRepository).should().save(any(ReviewLike.class));
        assertThat(review.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("한즐요약 좋아요 삭제 시 한줄요약 likeCount가 감소한다.")
    void givenReviewId_whenDeletingReviewLike_thenDeleteReviewLike() {
        //given
        Long reviewId = 1L;
        Long memberId = 1L;
        Review review = createReview(reviewId);
        Member member = createMember(memberId);
        ReviewLike reviewLike = createReviewLike(review, member);
        review.increaseLikeCount();

        given(reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId)).willReturn(Optional.of(reviewLike));
        willDoNothing().given(reviewLikeRepository).delete(reviewLike);

        //when
        sut.deleteLike(reviewId, memberId);

        //then
        then(reviewLikeRepository).should().findByReviewIdAndMemberId(reviewId, memberId);
        assertThat(review.getLikeCount()).isZero();
    }

}
