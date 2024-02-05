package cotato.bookitlist.post.domain;

import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public static PostLike of(Member member, Post post) {
        return new PostLike(member, post);
    }

    public void increasePostLikeCount() {
        post.increaseLikeCount();
    }

    public void decreasePostLikeCount(Member member, Post post) {
        if (!member.getId().equals(this.member.getId())) {
            throw new AccessDeniedException("권한이 없는 유저입니다.");
        }

        if (!post.getId().equals(this.post.getId())) {
            throw new IllegalArgumentException("해당 게시글의 좋아요가 아닙니다.");
        }

        post.decreaseLikeCount();
    }
}
