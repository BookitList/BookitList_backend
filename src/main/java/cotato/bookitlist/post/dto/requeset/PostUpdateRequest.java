package cotato.bookitlist.post.dto.requeset;

import cotato.bookitlist.post.domain.PostStatus;
import cotato.bookitlist.post.domain.PostTemplate;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static cotato.bookitlist.post.domain.PostTemplate.TEMPLATE;

public record PostUpdateRequest(
        @Size(max = 256)
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotNull
        PostStatus status,
        @NotNull
        PostTemplate template
) {
    @AssertTrue(message = "잘못된 템플릿 형식입니다.")
    public boolean isValidTemplateContent() {
        if (template.equals(TEMPLATE)) {
            return content.split(TEMPLATE.split).length == TEMPLATE.num;
        }
        return true;
    }
}
