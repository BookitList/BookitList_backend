package cotato.bookitlist.review.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.review.domain.Review;
import cotato.bookitlist.review.domain.ReviewLike;
import cotato.bookitlist.review.repository.ReviewLikeRepository;
import cotato.bookitlist.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;

    public Long registerLike(Long reviewId, Long memberId) {
        if (reviewLikeRepository.existsByReviewIdAndMemberId(reviewId, memberId)) {
            throw new DuplicateKeyException("한줄요약 좋아요가 이미 존재합니다");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));
        Member member = memberRepository.getReferenceById(memberId);

        ReviewLike reviewLike = ReviewLike.of(member, review);
        reviewLike.increaseReviewLikeCount();

        return reviewLikeRepository.save(reviewLike).getId();
    }

    public void deleteLike(Long reviewId, Long memberId) {
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약 좋아요 정보를 찾을 수 없습니다."));

        reviewLike.decreaseReviewLikeCount();

        reviewLikeRepository.delete(reviewLike);
    }
}
