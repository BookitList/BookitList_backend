package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.dto.PostDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostListResponse(
        int totalResults,
        int totalPages,
        int startIndex,
        int itemsPerPage,
        List<PostResponse> postList
) {

    public static PostListResponse from(Page<Post> page, Long memberId) {
        return new PostListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.stream().map(p -> PostResponse.from(p, memberId)).toList()
        );
    }

    public static PostListResponse fromDto(Page<PostDto> dtoPage, Long memberId) {
        return new PostListResponse(
                (int) dtoPage.getTotalElements(),
                dtoPage.getTotalPages(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.stream().map(p -> PostResponse.fromDto(p, memberId)).toList()
        );
    }
}
