package cotato.bookitlist.post.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.entity.Post;
import cotato.bookitlist.post.domain.entity.PostLike;
import cotato.bookitlist.post.repository.PostLikeRepository;
import cotato.bookitlist.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;

    public Long registerLike(Long postId, Long memberId) {
        if(postLikeRepository.existsByPostIdAndMemberId(postId, memberId)){
            throw new DuplicateKeyException("게시글 좋아요가 이미 존재합니다.");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Member member = memberRepository.getReferenceById(memberId);

        PostLike postLike = PostLike.of(member, post);
        postLike.increasePostLikeCount();

        return postLikeRepository.save(postLike).getId();
    }

    public void deleteLike(Long postId, Long memberId) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("게시글 좋아요 정보를 찾을 수 없습니다."));

        postLike.decreasePostLikeCount();

        postLikeRepository.delete(postLike);
    }
}
