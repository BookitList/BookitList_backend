package cotato.bookitlist.fixture;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.domain.entity.PostLike;
import org.springframework.test.util.ReflectionTestUtils;

public class PostLikeFixture {
    public static PostLike createPostLike(Post post, Member member) {
        PostLike postLike = PostLike.of(member, post);
        ReflectionTestUtils.setField(postLike, "id", 1L);
        return postLike;
    }
}
