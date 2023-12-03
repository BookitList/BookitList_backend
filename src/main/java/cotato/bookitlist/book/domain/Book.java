package cotato.bookitlist.book.domain;

import cotato.bookitlist.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE book SET deleted = true WHERE book_id = ?")
@Where(clause = "deleted = false")
public class Book extends BaseEntity {

    @Id
    @Column(name = "book_id")
    @GeneratedValue
    private Long id;

    private int markCount = 0;

    private boolean deleted = false;

}
