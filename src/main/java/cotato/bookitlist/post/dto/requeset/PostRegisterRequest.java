package cotato.bookitlist.post.dto.requeset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRegisterRequest(
        @NotNull
        Long bookId,
        @Size(max = 256)
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
