package cotato.bookitlist.post.controller;

import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{post-id}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Void> registerLike(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        Long postLikeId = postLikeService.registerLike(postId, details.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postLikeId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        postLikeService.deleteLike(postId, details.getId());

        return ResponseEntity.noContent().build();
    }
}
