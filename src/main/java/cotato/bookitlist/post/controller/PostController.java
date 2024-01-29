package cotato.bookitlist.post.controller;

import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.post.dto.PostRegisterRequest;
import cotato.bookitlist.post.dto.PostUpdateRequest;
import cotato.bookitlist.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> registerPost(
            @Valid @RequestBody PostRegisterRequest request,
            @AuthenticationPrincipal AuthDetails details
    ) {
        Long postId = postService.registerPost(request, details.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{post-id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable("post-id") Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal AuthDetails details
    ) {
        postService.updatePost(postId, request, details.getId());

        return ResponseEntity.ok().build();
    }

}
