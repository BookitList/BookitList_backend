package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.book.service.BookService;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.entity.Post;
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

    private final BookService bookService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public Long registerPost(PostRegisterRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Book book = bookRepository.findByIsbn13(request.isbn13())
                .orElseGet(() -> bookRepository.getReferenceById(
                        bookService.registerBook(request.isbn13())
                ));

        Post post = Post.of(member, book, request.title(), request.content(), request.status(), request.template());

        return postRepository.save(post).getId();
    }

    public void updatePost(Long postId, PostUpdateRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.updatePost(member, request.title(), request.content(), request.status());
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPost(Long postId, Long memberId) {
        return postRepository.findPublicPostDetailByPostId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllPost(Pageable pageable, Long memberId) {
        return PostListResponse.from(postRepository.findPublicPostAll(pageable), memberId);
    }

    @Transactional(readOnly = true)
    public PostListResponse searchPost(String isbn13, Long memberId, Long loginMemberId, Pageable pageable) {
        return PostListResponse.fromDto(postRepository.findPublicPostWithLikedByIsbn13(isbn13, memberId, loginMemberId, pageable), loginMemberId);
    }

    @Transactional(readOnly = true)
    public PostCountResponse getPostCount(String isbn13) {
        return PostCountResponse.of(postRepository.countPublicPostByBook_Isbn13(isbn13));
    }

    public void increaseViewCount(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.increaseViewCount();
    }

    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findByIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.deletePost();
    }
}
