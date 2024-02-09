package cotato.bookitlist.book.domain.entity;

import cotato.bookitlist.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE book SET deleted = true WHERE book_id = ?")
@SQLRestriction("deleted = false")
public class Book extends BaseEntity {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher;
    private LocalDate pubDate;
    private String description;
    private String link;
    @Column(unique = true)
    private String isbn13;
    private Integer price;
    private String cover;

    private int likeCount = 0;

    private boolean deleted = false;

    private Book(String title, String author, String publisher, LocalDate pubDate, String description, String link, String isbn13, Integer price, String cover) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.description = description;
        this.link = link;
        this.isbn13 = isbn13;
        this.price = price;
        this.cover = cover;
    }

    public static Book of(String title, String author, String publisher, LocalDate pubDate, String description, String link, String isbn13, Integer price, String cover) {
        return new Book(title, author, publisher, pubDate, description, link, isbn13, price, cover);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

}
