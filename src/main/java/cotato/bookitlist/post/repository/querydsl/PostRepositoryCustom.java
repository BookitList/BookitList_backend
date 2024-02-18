package cotato.bookitlist.post.repository.querydsl;

import cotato.bookitlist.post.dto.PostDetailDto;
import cotato.bookitlist.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    Page<PostDto> findPublicPostWithLikedByIsbn13(String isbn13, Long memberId, Long loginMemberId, Pageable pageable);

    Optional<PostDetailDto> findPostDetailByPostId(Long postId, Long memberId);

    Page<PostDto> findLikePostByMemberId(Long memberId, Pageable pageable);
}
