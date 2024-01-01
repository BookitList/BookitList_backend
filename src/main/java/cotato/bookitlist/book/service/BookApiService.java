package cotato.bookitlist.book.service;

import cotato.bookitlist.book.dto.BookApiDto;
import cotato.bookitlist.book.dto.response.BookApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookApiService {

    @Value("${api.aladin.key}")
    private String aladinKey;

    private final AladinService aladinService;
    private final BookApiCacheService bookApiCacheService;

    public BookApiResponse findListByKeyWordAndApi(String keyword, int start) {
        JSONObject json = new JSONObject(aladinService.findAllByQuery(aladinKey, keyword, "JS", start));

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

            bookApiCacheService.saveBookApiCache(bookApiDto);
        }

        return BookApiResponse.of(totalResults, startIndex, itemsPerPage, bookApiDtoList);
    }
}
