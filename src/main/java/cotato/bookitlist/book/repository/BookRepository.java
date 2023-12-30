package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.dto.response.BookApiResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

    BookApiResponse findListByKeyWordAndApi(String keyword, int start);

}
