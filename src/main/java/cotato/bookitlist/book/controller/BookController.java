package cotato.bookitlist.book.controller;

import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/external")
    public BookApiResponse searchExternal(@RequestParam("key-word") String keyWord, @RequestParam int start) {
        return bookService.searchExternal(keyWord, start);
    }

}
