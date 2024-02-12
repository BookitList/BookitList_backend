package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.config.security.oauth.AuthProvider;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static cotato.bookitlist.post.domain.PostStatus.PUBLIC;
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

    Post createPost(Long postId) {
        Post post = Post.of(createMember(), createBook(), "title", "content", PUBLIC);
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }

    Book createBook() {
        return Book.of("title",
                "author",
                "publisher",
                LocalDate.now(),
                "description",
                "link",
                "isbn13",
                10000,
                "cover");
    }

    Member createMember(Long memberId) {
        Member member = new Member("email", "name", "oauth2Id", AuthProvider.KAKAO);
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    Member createMember() {
        return createMember(1L);
    }

    PostLike createPostLike(Post post, Member member) {
        PostLike postLike = PostLike.of(member, post);
        ReflectionTestUtils.setField(postLike, "id", 1L);
        return postLike;
    }

}
