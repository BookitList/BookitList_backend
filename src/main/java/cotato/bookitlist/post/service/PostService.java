package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.dto.PostDetailDto;
import cotato.bookitlist.post.dto.requeset.PostRegisterRequest;
import cotato.bookitlist.post.dto.requeset.PostUpdateRequest;
import cotato.bookitlist.post.dto.response.PostCountResponse;
import cotato.bookitlist.post.dto.response.PostListResponse;
import cotato.bookitlist.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public Long registerPost(PostRegisterRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));

        return postRepository.save(
                Post.of(member, book, request.title(), request.content())
        ).getId();
    }

    public void updatePost(Long postId, PostUpdateRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.updatePost(member, request.title(), request.content());
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPost(Long postId, Long memberId) {
        return postRepository.findDetailByPostId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllPost(Pageable pageable) {
        return PostListResponse.from(postRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public PostListResponse searchPost(String isbn13, Long memberId, Pageable pageable) {
        return PostListResponse.fromDto(postRepository.findWithLikedByIsbn13(isbn13, memberId, pageable));
    }

    @Transactional(readOnly = true)
    public PostCountResponse getPostCount(String isbn13) {
        return PostCountResponse.of(postRepository.countByBook_Isbn13(isbn13));
    }
}
