package cotato.bookitlist.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NameChangeRequest(
        @NotBlank
        String name
) {
}
