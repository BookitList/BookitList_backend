package cotato.bookitlist.post.repository;

import cotato.bookitlist.post.domain.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);

    @Modifying
    @Query(value = "delete from PostLike p where p.post.id=:postId")
    void deleteAllByPostId(Long postId);
}
