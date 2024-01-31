package cotato.bookitlist.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest(
        @Size(max = 50)
        @NotBlank
        String content
){
}
