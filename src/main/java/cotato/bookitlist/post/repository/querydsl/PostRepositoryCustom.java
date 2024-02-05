package cotato.bookitlist.post.repository.querydsl;

import cotato.bookitlist.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostDto> findWithLikedByIsbn13(String isbn13, Long memberId, Pageable pageable);
}
