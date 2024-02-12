package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.domain.PostTemplate;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.dto.PostDto;

import java.util.List;

public record PostResponse(
        Long postId,
        Long memberId,
        Long bookId,
        String title,
        List<String> content,
        int likeCount,
        int viewCount,
        boolean liked,
        PostTemplate template
) {

    public static PostResponse from(Post entity) {
        return new PostResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getBook().getId(),
                entity.getTitle(),
                getContentList(entity.getContent(), entity.getTemplate()),
                entity.getLikeCount(),
                entity.getViewCount(),
                false,
                entity.getTemplate()
        );
    }

    public static PostResponse fromDto(PostDto dto) {
        return new PostResponse(
                dto.postId(),
                dto.memberId(),
                dto.bookId(),
                dto.title(),
                getContentList(dto.content(), dto.template()),
                dto.likeCount(),
                dto.viewCount(),
                dto.liked(),
                dto.template()
        );
    }

    private static List<String> getContentList(String content, PostTemplate template) {
        if (template != PostTemplate.NON) {
            return List.of(content.split(template.split));
        }
        return List.of(content);
    }
}
