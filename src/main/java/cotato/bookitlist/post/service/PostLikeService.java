package cotato.bookitlist.post.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import cotato.bookitlist.post.domain.Post;
import cotato.bookitlist.post.domain.PostLike;
import cotato.bookitlist.post.repository.PostLikeRepository;
import cotato.bookitlist.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));
        Member member = memberRepository.getReferenceById(memberId);

        PostLike postLike = PostLike.of(member, post);
        postLike.increasePostLikeCount();

        return postLikeRepository.save(postLike).getId();
    }
}
