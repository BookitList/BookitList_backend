package cotato.bookitlist.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Size(max = 256)
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
