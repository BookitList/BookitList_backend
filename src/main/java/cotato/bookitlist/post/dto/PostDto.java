package cotato.bookitlist.post.dto;

import cotato.bookitlist.post.domain.PostTemplate;
import cotato.bookitlist.post.domain.entity.Post;

public record PostDto(
        Long postId,
        Long memberId,
        Long bookId,
        String title,
        String content,
        int likeCount,
        int viewCount,
        boolean liked,
        PostTemplate template
) {

    public static PostDto from(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getMember().getId(),
                entity.getBook().getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getLikeCount(),
                entity.getViewCount(),
                false,
                entity.getTemplate()
        );
    }
}
