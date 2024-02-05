package cotato.bookitlist.post.domain;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.access.AccessDeniedException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE post_id = ?")
@SQLRestriction("deleted = false")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int likeCount = 0;

    private int viewCount = 0;

    private boolean deleted = false;

    private Post(Member member, Book book, String title, String content) {
        this.member = member;
        this.book = book;
        this.title = title;
        this.content = content;
    }

    public static Post of(Member member, Book book, String title, String content) {
        return new Post(member, book, title, content);
    }

    public void updatePost(Member member, String title, String content) {
        if (!this.member.getId().equals(member.getId())) {
            throw new AccessDeniedException("권한이 없는 유저입니다.");
        }

        this.title = title;
        this.content = content;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
