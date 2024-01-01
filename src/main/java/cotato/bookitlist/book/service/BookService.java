package cotato.bookitlist.book.service;

import cotato.bookitlist.book.dto.response.BookApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookApiService bookApiService;

    public BookApiResponse searchExternal(String keyWord, int start) {
        return bookApiService.findListByKeyWordAndApi(keyWord, start);
    }
}
