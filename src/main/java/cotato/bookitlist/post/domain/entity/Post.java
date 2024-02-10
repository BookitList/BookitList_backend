package cotato.bookitlist.post.domain.entity;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.post.domain.PostStatus;
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

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private int likeCount = 0;

    private int viewCount = 0;

    private boolean deleted = false;

    private Post(Member member, Book book, String title, String content, PostStatus status) {
        this.member = member;
        this.book = book;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public static Post of(Member member, Book book, String title, String content, PostStatus status) {
        return new Post(member, book, title, content, status);
    }

    public void updatePost(Member member, String title, String content, PostStatus status) {
        if (!this.member.getId().equals(member.getId())) {
            throw new AccessDeniedException("권한이 없는 유저입니다.");
        }

        this.title = title;
        this.content = content;
        this.status = status;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
