package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    @Value("${api.aladin.key}")
    private String aladinKey;

    private final AladinRepository aladinRepository;

    public BookApiResponse findListByKeyWordAndApi(String keyword, int start) {
        JSONObject json = new JSONObject(aladinRepository.findAllByQuery(aladinKey, keyword, "JS", start));

        int totalResults = json.getInt("totalResults");
        int startIndex = json.getInt("startIndex");
        int itemsPerPage = json.getInt("itemsPerPage");

        JSONArray items = json.getJSONArray("item");

        List<BookApiDto> bookApiDtoList = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            BookApiDto bookApiDto = BookApiDto.from(
                    item.optString("title", ""),
                    item.optString("author", ""),
                    item.optString("pubDate", ""),
                    item.optString("description", ""),
                    item.optString("link", ""),
                    item.optString("isbn13", ""),
                    item.optIntegerObject("priceSales", null),
                    item.optString("cover", "")
            );

            bookApiDtoList.add(bookApiDto);
        }

        return BookApiResponse.of(totalResults, startIndex, itemsPerPage, bookApiDtoList);
    }
}
