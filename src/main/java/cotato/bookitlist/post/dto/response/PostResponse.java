package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.domain.PostStatus;
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
        boolean isMine,
        PostStatus postStatus,
        PostTemplate template
) {

    public static PostResponse from(Post entity, Long memberId) {
        return new PostResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getBook().getId(),
                entity.getTitle(),
                getContentList(entity.getContent(), entity.getTemplate()),
                entity.getLikeCount(),
                entity.getViewCount(),
                false,
                entity.getMember().getId().equals(memberId),
                entity.getStatus(),
                entity.getTemplate()
        );
    }

    public static PostResponse fromDto(PostDto dto, Long memberId) {
        return new PostResponse(
                dto.postId(),
                dto.memberId(),
                dto.bookId(),
                dto.title(),
                getContentList(dto.content(), dto.template()),
                dto.likeCount(),
                dto.viewCount(),
                dto.liked(),
                dto.memberId().equals(memberId),
                dto.postStatus(),
                dto.template()
        );
    }

    private static List<String> getContentList(String content, PostTemplate template) {
        if (template != cotato.bookitlist.post.domain.PostTemplate.NON) {
            return List.of(content.split(template.split));
        }
        return List.of(content);
    }
}
