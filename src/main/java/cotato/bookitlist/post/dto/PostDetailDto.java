package cotato.bookitlist.post.dto;

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
        boolean liked
) {
}