package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Long> {

    @Query("select p from Post p where p.status = 'PUBLIC'")
    Page<Post> findPublicPostAll(Pageable pageable);

    @Query("select count(p) from Post p where p.status = 'PUBLIC' and p.book.isbn13 = :isbn13")
    int countPublicPostByBook_Isbn13(String isbn13);
}
