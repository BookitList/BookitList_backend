package cotato.bookitlist.post.service;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.repository.BookRepository;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.dto.PostDto;
import cotato.bookitlist.post.dto.requeset.PostRegisterRequest;
import cotato.bookitlist.post.dto.requeset.PostUpdateRequest;
import cotato.bookitlist.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    public PostDto getPost(Long postId) {
        return PostDto.from(postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다.")));
    }
}
