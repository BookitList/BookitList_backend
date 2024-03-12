package cotato.bookitlist.review.controller;

import cotato.bookitlist.security.jwt.AuthDetails;
import cotato.bookitlist.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews/{review-id}/likes")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping
    public ResponseEntity<Void> registerLike(
            @PathVariable("review-id") Long reviewId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        Long reviewLikeId = reviewLikeService.registerLike(reviewId, details.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reviewLikeId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(
            @PathVariable("review-id") Long reviewId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        reviewLikeService.deleteLike(reviewId, details.getId());

        return ResponseEntity.noContent().build();
    }
}
