package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.dto.ReviewDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record ReviewListResponse(
        int totalResults,
        int totalPages,
        int startIndex,
        int itemsPerPage,
        List<ReviewResponse> reviewList
) {
    public static ReviewListResponse from(Page<Review> page, Long memberId) {
        return new ReviewListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.stream().map(r -> ReviewResponse.from(r, memberId)).toList()
        );
    }

    public static ReviewListResponse fromDto(Page<ReviewDto> page, Long memberId) {
        return new ReviewListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.stream().map(r -> ReviewResponse.from(r, memberId)).toList()
        );
    }
}
