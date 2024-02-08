package cotato.bookitlist.book.service;

import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.response.BookApiListResponse;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookApiComponent {

    @Value("${api.aladin.key}")
    private String aladinKey;

    private final AladinComponent aladinComponent;
    private final BookApiCacheService bookApiCacheService;

    public BookApiListResponse findListByKeyWordAndApi(String keyword, int start, int maxResults) {
        JSONObject json = new JSONObject(aladinComponent.findAllByQuery(aladinKey, keyword, "JS", start, maxResults, 20131101));

        int totalResults = json.getInt("totalResults");
        int startIndex = json.getInt("startIndex");
        int itemsPerPage = json.getInt("itemsPerPage");

        JSONArray items = json.getJSONArray("item");

        List<BookApiDto> bookApiDtoList = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            BookApiDto bookApiDto = BookApiDto.of(
                    item.optString("title", ""),
                    item.optString("author", ""),
                    item.optString("publisher", ""),
                    item.optString("pubDate", ""),
                    item.optString("description", ""),
                    item.optString("link", ""),
                    item.optString("isbn13", ""),
                    item.optIntegerObject("priceSales", null),
                    item.optString("cover", "")
            );

            bookApiDtoList.add(bookApiDto);

            bookApiCacheService.saveBookApiCache(bookApiDto);
        }

        return BookApiListResponse.of(totalResults, startIndex, itemsPerPage,
                bookApiDtoList.stream().map(BookApiResponse::from).toList());
    }

    public BookApiDto findByIsbn13(String isbn13) {
        JSONObject json = new JSONObject(aladinComponent.findByIsbn13(aladinKey, isbn13, "ISBN13", "JS", 20131101));

        if (json.has("errorMessage")) {
            throw new IllegalArgumentException("존재하지 않는 isbn13 입니다.");
        }

        JSONObject item = json.getJSONArray("item").getJSONObject(0);

        BookApiDto bookApiDto = BookApiDto.of(
                item.optString("title", ""),
                item.optString("author", ""),
                item.optString("publisher", ""),
                item.optString("pubDate", ""),
                item.optString("description", ""),
                item.optString("link", ""),
                item.optString("isbn13", ""),
                item.optIntegerObject("priceSales", null),
                item.optString("cover", "")
        );

        bookApiCacheService.saveBookApiCache(bookApiDto);

        return bookApiDto;
    }
}
