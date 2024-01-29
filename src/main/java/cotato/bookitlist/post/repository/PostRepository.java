package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
