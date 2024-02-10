package cotato.bookitlist.post.dto.requeset;

import cotato.bookitlist.post.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Size(max = 256)
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotNull
        PostStatus status
) {
}
