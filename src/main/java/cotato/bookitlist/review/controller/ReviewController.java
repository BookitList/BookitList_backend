package cotato.bookitlist.review.controller;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import cotato.bookitlist.common.domain.RecommendType;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
import cotato.bookitlist.review.dto.response.ReviewCountResponse;
import cotato.bookitlist.review.dto.response.ReviewDetailResponse;
import cotato.bookitlist.review.dto.response.ReviewListResponse;
import cotato.bookitlist.review.dto.response.ReviewSimpleResponse;
import cotato.bookitlist.review.service.ReviewFacade;
import cotato.bookitlist.review.service.ReviewService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private static final Long DEFAULT_USER_ID = 0L;
    private static final String REVIEW_VIEW_COOKIE_NAME = "review_view";

    private final ReviewService reviewService;
    private final ReviewFacade reviewFacade;

    @PostMapping
    public ResponseEntity<Void> registerReview(
            @Valid @RequestBody ReviewRegisterRequest reviewRegisterRequest,
            @AuthenticationPrincipal AuthDetails details
    ) {
        Long reviewId = reviewService.registerReview(reviewRegisterRequest, details.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reviewId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{review-id}")
    public ResponseEntity<Void> updateReview(
            @PathVariable(value = "review-id") Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest,
            @AuthenticationPrincipal AuthDetails details
    ) {
        reviewService.updateReview(reviewId, reviewUpdateRequest, details.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{review-id}")
    public ResponseEntity<ReviewDetailResponse> getReview(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("review-id") Long reviewId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        ResponseEntity<ReviewDetailResponse> responseEntity;
        if (details == null) {
            responseEntity = ResponseEntity.ok(ReviewDetailResponse.from(reviewService.getReview(reviewId, DEFAULT_USER_ID), DEFAULT_USER_ID));
        } else {
            responseEntity = ResponseEntity.ok(ReviewDetailResponse.from(reviewService.getReview(reviewId, details.getId()), details.getId()));
        }

        handleReviewViewCount(request, response, reviewId);
        return responseEntity;
    }

    @GetMapping("/all")
    public ResponseEntity<ReviewListResponse> getAllReview(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(reviewService.getAllReview(pageable, DEFAULT_USER_ID));
        }
        return ResponseEntity.ok(reviewService.getAllReview(pageable, details.getId()));
    }

    @GetMapping
    public ResponseEntity<ReviewListResponse> searchReview(
            @IsValidIsbn @RequestParam(required = false) String isbn13,
            @RequestParam(name = "member-id", required = false) Long memberId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(reviewService.searchReview(isbn13, memberId, DEFAULT_USER_ID, pageable));
        }
        return ResponseEntity.ok(reviewService.searchReview(isbn13, memberId, details.getId(), pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<ReviewCountResponse> getReviewCount(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(reviewService.getReviewCount(isbn13));
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("review-id") Long reviewId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        reviewFacade.deleteReview(reviewId, details.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/likes")
    public ResponseEntity<ReviewListResponse> searchLikeReview(
            @AuthenticationPrincipal AuthDetails details,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.searchLikeReview(details.getId(), pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<ReviewListResponse> getMyReviews(
            @AuthenticationPrincipal AuthDetails details,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getMyReviews(details.getId(), pageable));
    }

    @GetMapping("/recommend")
    public ResponseEntity<ReviewListResponse> getRecommendReviews(
            @RequestParam RecommendType type,
            @RequestParam int start,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(reviewService.getRecommendReviews(type, start, DEFAULT_USER_ID));
        }
        return ResponseEntity.ok(reviewService.getRecommendReviews(type, start, details.getId()));
    }

    @GetMapping("/best")
    public ResponseEntity<ReviewSimpleResponse> getBestReviewOfBook(
            @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(reviewService.getBestReviewOfBook(isbn13));
    }

    private void handleReviewViewCount(HttpServletRequest request, HttpServletResponse response, Long reviewId) {
        Cookie[] cookies = request.getCookies();
        Cookie reviewViewCookie = findCookie(cookies);

        if (reviewViewCookie != null) {
            if (!reviewViewCookie.getValue().contains("[" + reviewId + "]")) {
                reviewViewCookie.setValue(reviewViewCookie.getValue() + "[" + reviewId + "]");
                reviewService.increaseViewCount(reviewId);
                reviewViewCookie.setPath("/reviews");
            }
            response.addCookie(reviewViewCookie);
        } else {
            Cookie newCookie = new Cookie(REVIEW_VIEW_COOKIE_NAME, "[" + reviewId + "]");
            newCookie.setPath("/reviews");
            reviewService.increaseViewCount(reviewId);
            response.addCookie(newCookie);
        }
    }

    private Cookie findCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> REVIEW_VIEW_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
    }
}
