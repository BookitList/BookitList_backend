package cotato.bookitlist.book.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record BookApiDto(
        String title,
        String author,
        LocalDate pubDate,
        String description,
        String link,
        String isbn13,
        Integer price,
        String cover
) {

    public static BookApiDto from(
            String title,
            String author,
            String pubDateString,
            String description,
            String link,
            String isbn13,
            Integer price,
            String coverString
    ) {

        String cover = coverString.replace("cover", "cover500");

        LocalDate pubDate = null;
        if (!pubDateString.isEmpty()) {
            pubDate = LocalDate.parse(pubDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return new BookApiDto(title, author, pubDate, description, link, isbn13, price, cover);
    }


}
