package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Long> {

    int countByBook_Isbn13(String isbn13);
}
