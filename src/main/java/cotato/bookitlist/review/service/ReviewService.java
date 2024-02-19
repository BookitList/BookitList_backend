package cotato.bookitlist.review.service;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.book.service.BookService;
import cotato.bookitlist.common.domain.RecommendType;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.dto.ReviewDetailDto;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
import cotato.bookitlist.review.dto.response.ReviewCountResponse;
import cotato.bookitlist.review.dto.response.ReviewListResponse;
import cotato.bookitlist.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    @Value("${recommend.count.review}")
    private int recommendCount;

    private final BookService bookService;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public Long registerReview(ReviewRegisterRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Book book = bookRepository.findByIsbn13(request.isbn13())
                .orElseGet(() -> bookRepository.getReferenceById(
                        bookService.registerBook(request.isbn13())
                ));

        Review review = Review.of(member, book, request.content(), request.status());

        return reviewRepository.save(review).getId();
    }

    public void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));

        review.updateReview(member, reviewUpdateRequest.content(), reviewUpdateRequest.status());
    }

    @Transactional(readOnly = true)
    public ReviewDetailDto getReview(Long reviewId, Long memberId) {
        return reviewRepository.findReviewDetailByReviewId(reviewId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getAllReview(Pageable pageable, Long memberId) {
        return ReviewListResponse.from(reviewRepository.findPublicReviewAll(pageable), memberId);
    }

    @Transactional(readOnly = true)
    public ReviewListResponse searchReview(String isbn13, Long memberId, Long loginMemberId, Pageable pageable) {
        return ReviewListResponse.fromDto(
                reviewRepository.findPublicReviewWithLikedByIsbn13(isbn13, memberId, loginMemberId, pageable),
                loginMemberId
        );
    }

    @Transactional(readOnly = true)
    public ReviewCountResponse getReviewCount(String isbn13) {
        return ReviewCountResponse.of(reviewRepository.countPublicReviewByBook_Isbn13(isbn13));
    }

    public void increaseViewCount(Long reviewId) {
        Review review = reviewRepository.getReferenceById(reviewId);
        review.increaseViewCount();
    }

    public void deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findByIdAndMemberId(reviewId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));

        review.deleteReview();
    }

    @Transactional(readOnly = true)
    public ReviewListResponse searchLikeReview(Long memberId, Pageable pageable) {
        return ReviewListResponse.fromDto(reviewRepository.findLikeReviewByMemberId(memberId, pageable), memberId);
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getMyReviews(Long memberId, Pageable pageable) {
        return ReviewListResponse.from(reviewRepository.findByMemberId(memberId, pageable), memberId);
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getRecommendReviews(RecommendType type, int start, Long memberId) {
        return switch (type) {
            case LIKE -> getMostLikeReviews(start, memberId);
            case NEW -> getNewReviews(start, memberId);
        };
    }

    public ReviewListResponse getMostLikeReviews(int start, Long memberId) {
        Pageable pageable = PageRequest.of(start, recommendCount, Sort.by("likeCount").descending());
        Page<Review> reviewPage = reviewRepository.findPublicReviewAll(pageable);

        return ReviewListResponse.from(reviewPage, memberId);
    }

    public ReviewListResponse getNewReviews(int start, Long memberId) {
        Pageable pageable = PageRequest.of(start, recommendCount, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findPublicReviewAll(pageable);

        return ReviewListResponse.from(reviewPage, memberId);
    }
}
