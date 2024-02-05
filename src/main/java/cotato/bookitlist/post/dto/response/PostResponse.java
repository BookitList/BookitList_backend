package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.dto.PostDetailDto;

import java.time.LocalDateTime;

public record PostResponse(
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
        boolean isMine
) {

    public static PostResponse from(PostDetailDto dto, Long memberId) {
        return new PostResponse(
                dto.postId(),
                dto.memberId(),
                dto.bookId(),
                dto.title(),
                dto.content(),
                dto.likeCount(),
                dto.viewCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.liked(),
                dto.memberId().equals(memberId)
        );
    }
}
