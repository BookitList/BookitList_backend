package cotato.bookitlist.post.dto.requeset;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import cotato.bookitlist.post.domain.PostStatus;
import cotato.bookitlist.post.domain.PostTemplate;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static cotato.bookitlist.post.domain.PostTemplate.*;

public record PostRegisterRequest(
        @NotNull
        @IsValidIsbn
        String isbn13,
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

    @Hidden
    @AssertTrue(message = "잘못된 템플릿 형식입니다.")
    public boolean isValidTemplateContent() {
        if (template.equals(TEMPLATE)) {
            return content.split(TEMPLATE.split).length == TEMPLATE.num;
        }
        return true;
    }
}
