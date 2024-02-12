package cotato.bookitlist.post.dto.response;

import cotato.bookitlist.post.domain.PostTemplate;
import cotato.bookitlist.post.dto.PostDetailDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long postId,
        Long memberId,
        Long bookId,
        String title,
        List<String> content,
        int likeCount,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        boolean liked,
        boolean isMine,
        PostTemplate template
) {

    public static PostDetailResponse from(PostDetailDto dto, Long memberId) {
        return new PostDetailResponse(
                dto.postId(),
                dto.memberId(),
                dto.bookId(),
                dto.title(),
                getContentList(dto.content(), dto.template()),
                dto.likeCount(),
                dto.viewCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.liked(),
                dto.memberId().equals(memberId),
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
