package cotato.bookitlist.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("게시글 파사드 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostFacadeTest {

    @InjectMocks
    PostFacade sut;
    @Mock
    PostService postService;
    @Mock
    PostLikeService postLikeService;

    @Test
    @DisplayName("게시물 삭제요청시 게시물을 삭제한다")
    void givenPostId_whenDeletingPost_thenDeletePost() throws Exception{
        //given
        Long postId = 1L;
        Long memberId = 1L;
        willDoNothing().given(postLikeService).deleteAllPostLike(postId);
        willDoNothing().given(postService).deletePost(postId, memberId);

        //when
        sut.deletePost(postId, memberId);

        //then
        then(postLikeService).should().deleteAllPostLike(postId);
        then(postService).should().deletePost(postId, memberId);
    }


}
