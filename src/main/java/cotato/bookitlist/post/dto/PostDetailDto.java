package cotato.bookitlist.post.dto;

import cotato.bookitlist.post.domain.PostStatus;
import cotato.bookitlist.post.domain.PostTemplate;

import java.time.LocalDateTime;

public record PostDetailDto(
        Long postId,
        Long memberId,
        Long bookId,
        String title,
        String content,
        int likeCount,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        boolean liked,
        PostStatus postStatus,
        PostTemplate template
) {
}
