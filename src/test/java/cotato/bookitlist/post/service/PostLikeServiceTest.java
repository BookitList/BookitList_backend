package cotato.bookitlist.post.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.domain.entity.PostLike;
import cotato.bookitlist.post.repository.PostLikeRepository;
import cotato.bookitlist.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static cotato.bookitlist.fixture.PostFixture.createPost;
import static cotato.bookitlist.fixture.PostLikeFixture.createPostLike;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@DisplayName("게시글 좋아요 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @InjectMocks
    PostLikeService sut;
    @Mock
    PostRepository postRepository;
    @Mock
    PostLikeRepository postLikeRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("게시글 좋아요를 생성시 게시글 likeCount가 증가한다.")
    void givenPostId_whenRegisteringPostLike_thenRegisterPostLike() {
        //given
        Long postId = 1L;
        Long memberId = 1L;
        Post post = createPost(postId);
        Member member = createMember(memberId);

        given(postLikeRepository.existsByPostIdAndMemberId(postId, memberId)).willReturn(false);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.getReferenceById(anyLong())).willReturn(member);
        given(postLikeRepository.save(any(PostLike.class))).willReturn(createPostLike(post, member));

        //when
        sut.registerLike(postId, memberId);

        //then
        then(postLikeRepository).should().existsByPostIdAndMemberId(postId, memberId);
        then(postRepository).should().findById(anyLong());
        then(memberRepository).should().getReferenceById(anyLong());
        then(postLikeRepository).should().save(any(PostLike.class));
        assertThat(post.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 좋아요를 삭제시 게시글 likeCount가 감소한다.")
    void givenPostLikeId_whenDeletingPostLike_thenDeletePostLike() {
        //given
        Long postId = 1L;
        Long memberId = 1L;
        Post post = createPost(postId);
        Member member = createMember(memberId);
        PostLike postLike = createPostLike(post, member);
        post.increaseLikeCount();

        given(postLikeRepository.findByPostIdAndMemberId(postId, memberId)).willReturn(Optional.of(postLike));
        willDoNothing().given(postLikeRepository).delete(postLike);

        //when
        sut.deleteLike(postId, memberId);

        //then
        then(postLikeRepository).should().findByPostIdAndMemberId(postId, memberId);
        assertThat(post.getLikeCount()).isZero();
    }

    @Test
    @DisplayName("게시글 삭제시 모든 게시글 좋아요를 삭제한다.")
    void givenPostId_whenDeletingPost_thenDeleteAllPostLike() throws Exception{
        //given
        Long postId = 1L;
        willDoNothing().given(postLikeRepository).deleteAllByPostId(postId);

        //when
        sut.deleteAllPostLike(postId);

        //then
        then(postLikeRepository).should().deleteAllByPostId(postId);
    }


}
