package cotato.bookitlist.post.domain;

import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
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
}