package cotato.bookitlist.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final PostLikeService postLikeService;

    public void deletePost(Long postId, Long memberId) {
        postLikeService.deleteAllPostLike(postId);
        postService.deletePost(postId, memberId);
    }
}
