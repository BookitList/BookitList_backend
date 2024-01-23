package cotato.bookitlist.book.controller;

import cotato.bookitlist.book.dto.request.BookRegisterRequest;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.dto.response.BookListResponse;
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

    @GetMapping("/external/search")
    public ResponseEntity<BookApiListResponse> searchExternal(
            @RequestParam("keyword") String keyword,
            @RequestParam Integer start) {
        return ResponseEntity.ok(bookService.searchExternal(keyword, start));
    }

    @GetMapping("/external")
    public ResponseEntity<BookApiResponse> findExternal(
            @IsValidIsbn @RequestParam("isbn13") String isbn13
    ) {
        return ResponseEntity.ok(BookApiResponse.fromBookApiDto(bookService.findExternal(isbn13)));
    }

    @GetMapping
    public ResponseEntity<BookListResponse> search(
            @RequestParam(value = "isbn13", required = false) String isbn13,
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(hidden = true) @PageableDefault(sort = "pubDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (isbn13 != null) {
            return ResponseEntity.ok(bookService.search(isbn13).toBookResponse());
        } else if (keyword != null) {
            return ResponseEntity.ok(BookListResponse.from(bookService.search(keyword, pageable)));
        } else {
            throw new IllegalArgumentException("isbn13 또는 keyword를 입력하세요.");
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


