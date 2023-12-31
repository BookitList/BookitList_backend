package cotato.bookitlist.book.service;

import cotato.bookitlist.book.dto.response.BookApiResponse;
import cotato.bookitlist.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookApiResponse searchExternal(String keyWord, int start) {
        return bookRepository.findListByKeyWordAndApi(keyWord, start);
    }
}
