package cotato.bookitlist.book.controller;

import cotato.bookitlist.book.dto.request.BookIsbn13Request;
import cotato.bookitlist.book.service.BookLikeService;
import cotato.bookitlist.config.security.jwt.AuthDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/books/likes")
@RequiredArgsConstructor
public class BookLikeController {

    private final BookLikeService bookLikeService;

    @PostMapping
    public ResponseEntity<Void> registerLike(
            @Valid @RequestBody BookIsbn13Request request,
            @AuthenticationPrincipal AuthDetails details
    ) {
        Long bookLikeId = bookLikeService.registerLike(request.isbn13(), details.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookLikeId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(
            @Valid @RequestBody BookIsbn13Request request,
            @AuthenticationPrincipal AuthDetails details
    ) {
        bookLikeService.deleteLike(request.isbn13(), details.getId());

        return ResponseEntity.noContent().build();
    }

}
