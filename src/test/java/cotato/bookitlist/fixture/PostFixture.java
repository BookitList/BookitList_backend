package cotato.bookitlist.fixture;

import cotato.bookitlist.post.domain.entity.Post;
import org.springframework.test.util.ReflectionTestUtils;

import static cotato.bookitlist.fixture.BookFixture.createBook;
import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static cotato.bookitlist.post.domain.PostStatus.PUBLIC;
import static cotato.bookitlist.post.domain.PostTemplate.NON;

public class PostFixture {

    public static Post createPost(Long postId, Long memberId) {
        Post post = Post.of(createMember(memberId), createBook(), "title", "content", PUBLIC, NON);
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }

    public static Post createPost(Long postId) {
        return createPost(postId, 1L);
    }

}
