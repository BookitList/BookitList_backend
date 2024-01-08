package cotato.bookitlist.book.controller;

import cotato.bookitlist.book.dto.request.BookRegisterRequest;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/external")
    public ResponseEntity<BookApiResponse> searchExternal(
            @RequestParam(value = "isbn13", required = false) String isbn13,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(required = false) Integer start) {
        if (isbn13 != null) {
            return ResponseEntity.ok(bookService.searchExternal(isbn13).toBookApiResponse());
        } else if (keyword != null && start != null) {
            return ResponseEntity.ok(bookService.searchExternal(keyword, start));
        } else {
            throw new IllegalArgumentException("isbn13 또는 keyword와 start를 함께 입력하세요.");
        }
    }

    @PostMapping
    public ResponseEntity<Void> registerBook(
            @Valid @RequestBody BookRegisterRequest request
    ) {
        Long bookId = bookService.registerBook(request.isbn13());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookId)
                .toUri();

        return ResponseEntity.created(location).build();
    }


}
