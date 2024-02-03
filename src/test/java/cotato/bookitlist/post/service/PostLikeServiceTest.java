package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.domain.PostLike;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
    @DisplayName("게시글 좋아요를 생성한다.")
    void givenPostId_whenRegisteringPostLike_thenRegisterPostLike() throws Exception {
        //given
        Long postId = 1L;
        Long memberId = 1L;
        Post post = createPost();
        Member member = createMember();

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.getReferenceById(anyLong())).willReturn(member);
        given(postLikeRepository.save(any(PostLike.class))).willReturn(createPostLike(post, member));

        //when
        sut.registerLike(postId, memberId);

        //then
        assertThat(post.getLikeCount()).isEqualTo(1);
        then(postRepository).should().findById(anyLong());
        then(memberRepository).should().getReferenceById(anyLong());
        then(postLikeRepository).should().save(any(PostLike.class));
    }

    Post createPost() {
        return Post.of(createMember(), createBook(), "title", "content");
    }

    Book createBook() {
        return Book.of("title", "author", "pubisher", LocalDate.now(), "description", "link", "isbn13", 10000, "cover");
    }

    Member createMember() {
        return new Member("email", "name", "oauth2Id", AuthProvider.KAKAO);
    }

    PostLike createPostLike(Post post, Member member) {
        PostLike postLike = PostLike.of(member, post);
        ReflectionTestUtils.setField(postLike, "id", 1L);
        return postLike;
    }

}