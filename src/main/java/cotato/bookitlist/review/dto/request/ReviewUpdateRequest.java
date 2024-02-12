package cotato.bookitlist.review.dto.request;

import cotato.bookitlist.review.domain.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest(
        @Size(max = 50)
        @NotBlank
        String content,

        @NotNull
        ReviewStatus status
){
}
