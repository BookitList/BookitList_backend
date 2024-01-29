package cotato.bookitlist.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRegisterRequest(
        @NotNull
        Long bookId,
        @Size(max = 50)
        @NotBlank
        String content
) {
}
