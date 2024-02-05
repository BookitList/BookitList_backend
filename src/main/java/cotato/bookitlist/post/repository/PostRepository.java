package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Long> {
    Page<Post> findByBook_Isbn13(String isbn13, Pageable pageable);

    int countByBook_Isbn13(String isbn13);
}
