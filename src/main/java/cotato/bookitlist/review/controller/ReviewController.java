package cotato.bookitlist.review.controller;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
import cotato.bookitlist.review.dto.response.ReviewCountResponse;
import cotato.bookitlist.review.dto.response.ReviewListResponse;
import cotato.bookitlist.review.dto.response.ReviewResponse;
import cotato.bookitlist.review.service.ReviewService;
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

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private static final Long DEFAULT_USER_ID = 0L;

    private final ReviewService reviewService;

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
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable("review-id") Long reviewId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(ReviewResponse.from(reviewService.getReview(reviewId, DEFAULT_USER_ID), DEFAULT_USER_ID));
        }
        return ResponseEntity.ok(ReviewResponse.from(reviewService.getReview(reviewId, details.getId()), details.getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<ReviewListResponse> getAllReview(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getAllReview(pageable));
    }

    @GetMapping
    public ResponseEntity<ReviewListResponse> searchReview(
            @IsValidIsbn @RequestParam String isbn13,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(reviewService.searchReview(isbn13, DEFAULT_USER_ID, pageable));
        }
        return ResponseEntity.ok(reviewService.searchReview(isbn13, details.getId(), pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<ReviewCountResponse> getReviewCount(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(reviewService.getReviewCount(isbn13));
    }
}
