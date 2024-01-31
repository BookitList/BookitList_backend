package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.dto.PostDto;

public record PostResponse(
        Long postId,
        Long memberId,
        Long bookId,
        String title,
        String content,
        int likeCount,
        int viewCount
) {

    public static PostResponse from(PostDto dto) {
        return new PostResponse(
                dto.postId(),
                dto.memberId(),
                dto.bookId(),
                dto.title(),
                dto.content(),
                dto.likeCount(),
                dto.viewCount()
        );
    }
}
