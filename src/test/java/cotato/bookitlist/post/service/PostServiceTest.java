package cotato.bookitlist.post.service;

import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static cotato.bookitlist.fixture.PostFixture.createPost;
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

    @Test
    @DisplayName("게시물 삭제 요청시 게시물을 삭제한다.")
    void givenPostId_whenDeletingPost_thenDeletePost() throws Exception {
        //given
        Long memberId = 1L;
        Long postId = 1L;
        Post post = createPost(postId, memberId);
        given(postRepository.findByIdAndMemberId(postId, memberId)).willReturn(Optional.of(post));

        //when
        sut.deletePost(postId, memberId);

        //then
        then(postRepository).should().findByIdAndMemberId(postId, memberId);
        assertThat(post.isDeleted()).isTrue();
        assertThat(post.getLikeCount()).isZero();
    }

}
