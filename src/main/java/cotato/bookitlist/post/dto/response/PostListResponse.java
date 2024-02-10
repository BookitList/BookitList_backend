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
        List<PostDto> postList
) {

    public static PostListResponse from(Page<Post> page) {
        return new PostListResponse(
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.stream().map(PostDto::from).toList()
        );
    }

    public static PostListResponse fromDto(Page<PostDto> dtoPage) {
        return new PostListResponse(
                (int) dtoPage.getTotalElements(),
                dtoPage.getTotalPages(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.stream().toList()
        );
    }
}
