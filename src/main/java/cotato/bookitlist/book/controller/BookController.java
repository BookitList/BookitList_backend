package cotato.bookitlist.book.controller;

import cotato.bookitlist.book.dto.request.BookRegisterRequest;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.dto.response.BookListResponse;
import cotato.bookitlist.book.dto.response.BookResponse;
import cotato.bookitlist.book.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<BookApiListResponse> searchExternal(
            @RequestParam String keyword,
            @RequestParam int start,
            @RequestParam(name = "max-results", defaultValue = "5") int maxResults
    ) {
        return ResponseEntity.ok(bookService.searchExternal(keyword, start, maxResults));
    }

    @GetMapping("/external")
    public ResponseEntity<BookApiResponse> getExternal(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(BookApiResponse.from(bookService.getExternal(isbn13)));
    }

    @Deprecated
    @GetMapping("/deprecated/search")
    public ResponseEntity<BookListResponse> search(
            @RequestParam String keyword,
            @Parameter(hidden = true) @PageableDefault(sort = "pubDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(BookListResponse.from(bookService.search(keyword, pageable)));
    }

    @GetMapping
    public ResponseEntity<BookResponse> getBookByIsbn13(
            @IsValidIsbn @RequestParam String isbn13
    ) {
        return ResponseEntity.ok(BookResponse.fromBookDto(bookService.getBookByIsbn13(isbn13)));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> getBook(
            @PathVariable("book-id") Long bookId
    ) {
        return ResponseEntity.ok(BookResponse.fromBookDto(bookService.getBook(bookId)));
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


