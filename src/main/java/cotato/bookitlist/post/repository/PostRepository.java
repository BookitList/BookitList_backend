package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Long> {

    @Query("select p from Post p where p.status = 'PUBLIC' and p.member.profileStatus = 'PUBLIC'")
    Page<Post> findPublicPostAll(Pageable pageable);

    @Query("select count(p) from Post p where p.status = 'PUBLIC' and p.book.isbn13 = :isbn13 and p.member.profileStatus = 'PUBLIC'")
    int countPublicPostByBook_Isbn13(String isbn13);

    Optional<Post> findByIdAndMemberId(Long postId, Long memberId);
}
