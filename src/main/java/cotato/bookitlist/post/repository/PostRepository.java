package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBook_Isbn13(String isbn13, Pageable pageable);

    int countByBook_Isbn13(String isbn13);
}
