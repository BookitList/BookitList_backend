package cotato.bookitlist.post.controller;

import cotato.bookitlist.book.controller.IsValidIsbn;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.post.dto.requeset.PostRegisterRequest;
import cotato.bookitlist.post.dto.requeset.PostUpdateRequest;
import cotato.bookitlist.post.dto.response.PostCountResponse;
import cotato.bookitlist.post.dto.response.PostListResponse;
import cotato.bookitlist.post.dto.response.PostResponse;
import cotato.bookitlist.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private static final Long DEFAULT_USER_ID = 0L;

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

    @GetMapping("/{post-id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(PostResponse.from(postService.getPost(postId, DEFAULT_USER_ID), DEFAULT_USER_ID));
        }
        return ResponseEntity.ok(PostResponse.from(postService.getPost(postId, details.getId()), details.getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAllPost(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(postService.getAllPost(pageable));
    }

    @GetMapping
    public ResponseEntity<PostListResponse> searchPost(
            @IsValidIsbn @RequestParam String isbn13,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(postService.searchPost(isbn13, DEFAULT_USER_ID, pageable));
        }

        return ResponseEntity.ok(postService.searchPost(isbn13, details.getId(), pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<PostCountResponse> getPostCount(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(postService.getPostCount(isbn13));
    }
}
