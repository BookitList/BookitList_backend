package cotato.bookitlist.post.controller;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import cotato.bookitlist.post.dto.request.PostRegisterRequest;
import cotato.bookitlist.post.dto.request.PostUpdateRequest;
import cotato.bookitlist.post.dto.response.PostCountResponse;
import cotato.bookitlist.post.dto.response.PostListResponse;
import cotato.bookitlist.post.dto.response.PostDetailResponse;
import cotato.bookitlist.post.service.PostFacade;
import cotato.bookitlist.post.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private static final Long DEFAULT_USER_ID = 0L;
    private static final String POST_VIEW_COOKIE_NAME = "post_view";

    private final PostService postService;
    private final PostFacade postFacade;

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
    public ResponseEntity<PostDetailResponse> getPost(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal AuthDetails details
    ) {
        ResponseEntity<PostDetailResponse> responseEntity;
        if (details == null) {
            responseEntity = ResponseEntity.ok(PostDetailResponse.from(postService.getPost(postId, DEFAULT_USER_ID), DEFAULT_USER_ID));
        } else {
            responseEntity = ResponseEntity.ok(PostDetailResponse.from(postService.getPost(postId, details.getId()), details.getId()));
        }

        handlePostViewCount(request, response, postId);
        return responseEntity;
    }

    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAllPost(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(postService.getAllPost(pageable, DEFAULT_USER_ID));
        }

        return ResponseEntity.ok(postService.getAllPost(pageable, details.getId()));
    }

    @GetMapping
    public ResponseEntity<PostListResponse> searchPost(
            @IsValidIsbn @RequestParam(required = false) String isbn13,
            @RequestParam(name = "member-id", required = false) Long memberId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthDetails details
    ) {
        if (details == null) {
            return ResponseEntity.ok(postService.searchPost(isbn13, memberId, DEFAULT_USER_ID, pageable));
        }

        return ResponseEntity.ok(postService.searchPost(isbn13, memberId, details.getId(), pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<PostCountResponse> getPostCount(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(postService.getPostCount(isbn13));
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal AuthDetails details
    ) {

        postFacade.deletePost(postId, details.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/likes")
    public ResponseEntity<PostListResponse> searchLikePost(
            @AuthenticationPrincipal AuthDetails details,
            Pageable pageable
    ) {
        return ResponseEntity.ok(postService.searchLikePost(details.getId(), pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<PostListResponse> getMyPosts(
            @AuthenticationPrincipal AuthDetails details,
            Pageable pageable
    ) {
        return ResponseEntity.ok(postService.getMyPosts(details.getId(), pageable));
    }

    private void handlePostViewCount(HttpServletRequest request, HttpServletResponse response, Long postId) {
        Cookie[] cookies = request.getCookies();
        Cookie postViewCookie = findCookie(cookies);

        if (postViewCookie != null) {
            if (!postViewCookie.getValue().contains("[" + postId + "]")) {
                postViewCookie.setValue(postViewCookie.getValue() + "[" + postId + "]");
                postService.increaseViewCount(postId);
                postViewCookie.setPath("/posts");
            }
            response.addCookie(postViewCookie);
        } else {
            Cookie newCookie = new Cookie(POST_VIEW_COOKIE_NAME, "[" + postId + "]");
            newCookie.setPath("/posts");
            postService.increaseViewCount(postId);
            response.addCookie(newCookie);
        }
    }

    private Cookie findCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> POST_VIEW_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
    }
}
