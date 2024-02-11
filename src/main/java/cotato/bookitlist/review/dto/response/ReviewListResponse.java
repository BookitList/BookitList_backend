package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.review.domain.Review;
import cotato.bookitlist.review.dto.ReviewDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record ReviewListResponse(
        int totalResults,
        int totalPages,
        int startIndex,
        int itemsPerPage,
        List<ReviewDto> reviewDtoList
) {
    public static ReviewListResponse from(Page<Review> page) {
        return new ReviewListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.stream().map(ReviewDto::from).toList()
        );
    }

    public static ReviewListResponse fromDto(Page<ReviewDto> page) {
        return new ReviewListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.toList()
        );
    }
}
