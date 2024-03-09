package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.book.service.BookService;
import cotato.bookitlist.common.domain.RecommendType;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.dto.PostDetailDto;
import cotato.bookitlist.post.dto.request.PostRegisterRequest;
import cotato.bookitlist.post.dto.request.PostUpdateRequest;
import cotato.bookitlist.post.dto.response.PostCountResponse;
import cotato.bookitlist.post.dto.response.PostListResponse;
import cotato.bookitlist.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    @Value("${recommend.count.post}")
    private int recommendCount;

    private final BookService bookService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Long registerPost(PostRegisterRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        //게시글 등록 시 해당 책이 데이터베이스에 있으면 해당 책에 게시글을 작성한다.
        //데이터베이스에 책이 없다면 알라딘 API 통신을 통해 책 정보를 가져와 저장 후 그 책에 게시글을 작성한다.
        //단, 알라딘 API 통신을 하기 전 Redis 에 책 정보가 저장되어 있을 수 있으므로 있다면 그 정보로 책을 저장한다.
        Book book = bookRepository.findByIsbn13(request.isbn13())
                .orElseGet(() -> bookRepository.getReferenceById(
                        bookService.registerBook(request.isbn13())
                ));

        Post post = Post.of(member, book, request.title(), request.content(), request.status(), request.template());

        return postRepository.save(post).getId();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.updatePost(member, request.title(), request.content(), request.status());
    }

    public PostDetailDto getPost(Long postId, Long memberId) {
        return postRepository.findPostDetailByPostId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public PostListResponse getAllPost(Pageable pageable, Long memberId) {
        return PostListResponse.from(postRepository.findPublicPostAll(pageable), memberId);
    }

    public PostListResponse searchPost(String isbn13, Long memberId, Long loginMemberId, Pageable pageable) {
        return PostListResponse.fromDto(
                postRepository.findPublicPostWithLikedByIsbn13(isbn13, memberId, loginMemberId, pageable),
                loginMemberId
        );
    }

    public PostCountResponse getPostCount(String isbn13) {
        return PostCountResponse.of(postRepository.countPublicPostByBook_Isbn13(isbn13));
    }

    @Transactional
    public void increaseViewCount(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.increaseViewCount();
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findByIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.deletePost();
    }

    public PostListResponse searchLikePost(Long memberId, Pageable pageable) {
        return PostListResponse.fromDto(postRepository.findLikePostByMemberId(memberId, pageable), memberId);
    }

    public PostListResponse getMyPosts(Long memberId, Pageable pageable) {
        return PostListResponse.from(postRepository.findByMemberId(memberId, pageable), memberId);
    }

    public PostListResponse getRecommendPosts(RecommendType recommendType, int start, Long memberId) {
        return switch (recommendType) {
            case LIKE -> getMostLikePosts(start, memberId);
            case NEW -> getNewPosts(start, memberId);
        };
    }

    public PostListResponse getMostLikePosts(int start, Long memberId) {
        Pageable pageable = PageRequest.of(start, recommendCount, Sort.by("likeCount").descending());
        Page<Post> postPage = postRepository.findPublicPostAll(pageable);

        return PostListResponse.from(postPage, memberId);
    }

    public PostListResponse getNewPosts(int start, Long memberId) {
        Pageable pageable = PageRequest.of(start, recommendCount, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findPublicPostAll(pageable);

        return PostListResponse.from(postPage, memberId);
    }

    @Transactional
    public void togglePostStatus(Long postId, Long memberId) {
        Post post = postRepository.findByIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.toggleStatus();
    }
}
