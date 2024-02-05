package cotato.bookitlist.review.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.review.domain.Review;
import cotato.bookitlist.review.dto.ReviewDto;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
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

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public Long registerReview(ReviewRegisterRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));

        Review review = Review.of(member, book, request.content());

        return reviewRepository.save(review).getId();
    }

    public void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다"));

        review.updateReview(member, reviewUpdateRequest.content());
    }

    public ReviewDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("한줄요약을 찾을 수 없습니다"));

        return ReviewDto.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getAllReview(Pageable pageable) {
        return ReviewListResponse.from(reviewRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public ReviewListResponse searchReview(String isbn13, Pageable pageable) {
        return ReviewListResponse.from(reviewRepository.findByBook_Isbn13(isbn13, pageable));
    }
}
