package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static cotato.bookitlist.post.domain.PostStatus.PUBLIC;
import static cotato.bookitlist.post.domain.PostTemplate.NON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("게시글 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService sut;
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 조회수 증가 요청시 조회수가 증가한다.")
    void givenPostId_whenIncreasingViewCount_thenIncreaseViewCount() {
        //given
        Long postId = 1L;
        Post post = createPost(postId);
        given(postRepository.getReferenceById(postId)).willReturn(post);

        //when
        sut.increaseViewCount(postId);

        //then
        then(postRepository).should().getReferenceById(postId);
        assertThat(post.getViewCount()).isEqualTo(1);
    }

    Post createPost(Long postId) {
        Post post = Post.of(createMember(), createBook(), "title", "content", PUBLIC, NON);
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }

    Book createBook() {
        return Book.of("title", "author", "publisher", LocalDate.now(), "description", "link", "isbn13", 10000, "cover");
    }

    Member createMember(Long memberId) {
        Member member = new Member("email", "name", "oauth2Id", AuthProvider.KAKAO);
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    Member createMember() {
        return createMember(1L);
    }
}
