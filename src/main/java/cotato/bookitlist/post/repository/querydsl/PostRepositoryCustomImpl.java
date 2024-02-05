package cotato.bookitlist.post.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.bookitlist.post.dto.PostDetailDto;
import cotato.bookitlist.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static cotato.bookitlist.post.domain.QPost.post;
import static cotato.bookitlist.post.domain.QPostLike.postLike;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostDto> findWithLikedByIsbn13(String isbn13, Long memberId, Pageable pageable) {
        List<PostDto> result = queryFactory
                .select(
                        Projections.constructor(
                                PostDto.class,
                                post.id,
                                post.member.id,
                                post.book.id,
                                post.title,
                                post.content,
                                post.likeCount,
                                post.viewCount,
                                Expressions.cases()
                                        .when(isLikedByMember(memberId, post.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked")
                        )
                )
                .from(post)
                .where(post.book.isbn13.eq(isbn13))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(post.book.isbn13.eq(isbn13))
                .from(post);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<PostDetailDto> findDetailByPostId(Long postId, Long memberId) {
       return Optional.ofNullable(queryFactory
                .select(
                        Projections.constructor(
                                PostDetailDto.class,
                                post.id,
                                post.member.id,
                                post.book.id,
                                post.title,
                                post.content,
                                post.likeCount,
                                post.viewCount,
                                post.createdAt,
                                post.modifiedAt,
                                Expressions.cases()
                                        .when(isLikedByMember(memberId, post.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked")
                        )
                )
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne());
    }

    private BooleanExpression isLikedByMember(Long memberId, NumberPath<Long> postId) {
        return JPAExpressions.selectOne()
                .from(postLike)
                .where(postLike.member.id.eq(memberId)
                        .and(postLike.post.id.eq(postId)))
                .exists();
    }
}
