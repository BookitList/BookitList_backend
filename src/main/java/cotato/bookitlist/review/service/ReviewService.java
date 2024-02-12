package cotato.bookitlist.review.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.book.service.BookService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

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

        Review review = Review.of(member, book, request.content());

        return reviewRepository.save(review).getId();
    }

    public void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));

        review.updateReview(member, reviewUpdateRequest.content());
    }

    @Transactional(readOnly = true)
    public ReviewDetailDto getReview(Long reviewId, Long memberId) {
        return reviewRepository.findReviewDetailByReviewId(reviewId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getAllReview(Pageable pageable) {
        return ReviewListResponse.from(reviewRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public ReviewListResponse searchReview(String isbn13, Long memberId, Pageable pageable) {
        return ReviewListResponse.fromDto(reviewRepository.findReviewWithLikedByIsbn13(isbn13, memberId, pageable));
    }

    @Transactional(readOnly = true)
    public ReviewCountResponse getReviewCount(String isbn13) {
        return ReviewCountResponse.of(reviewRepository.countByBook_Isbn13(isbn13));
    }

    @Transactional
    public void increaseViewCount(Long reviewId) {
        Review review = reviewRepository.getReferenceById(reviewId);
        review.increaseViewCount();
    }
}
