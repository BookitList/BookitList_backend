package cotato.bookitlist.post.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.bookitlist.member.domain.ProfileStatus;
import cotato.bookitlist.post.domain.PostStatus;
import cotato.bookitlist.post.dto.PostDetailDto;
import cotato.bookitlist.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static cotato.bookitlist.member.domain.QMember.member;
import static cotato.bookitlist.post.domain.entity.QPost.post;
import static cotato.bookitlist.post.domain.entity.QPostLike.postLike;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostDto> findPublicPostWithLikedByIsbn13(String isbn13, Long memberId, Long loginMemberId, Pageable pageable) {
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
                                        .when(isLikedByMember(loginMemberId, post.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked"),
                                post.status,
                                post.template
                        )
                )
                .from(post)
                .join(post.member, member)
                .where(isbnEq(isbn13), memberIdEq(memberId), post.status.eq(PostStatus.PUBLIC), post.member.status.eq(ProfileStatus.PUBLIC))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .join(post.member, member)
                .where(isbnEq(isbn13), memberIdEq(memberId));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<PostDetailDto> findPostDetailByPostId(Long postId, Long memberId) {
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
                                        .as("liked"),
                                post.status,
                                post.template
                        )
                )
                .from(post)
                .join(post.member, member)
                .where(post.id.eq(postId), buildPostAccessCondition(post.member.id, memberId))
                .fetchOne());
    }

    @Override
    public Page<PostDto> findLikePostByMemberId(Long memberId, Pageable pageable) {
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
                                Expressions.constant(true),
                                post.status,
                                post.template
                        )
                )
                .from(postLike)
                .join(postLike.post, post)
                .join(postLike.member, member)
                .where(postLike.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(postLike)
                .join(postLike.post, post)
                .join(postLike.member, member)
                .where(postLike.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isLikedByMember(Long memberId, NumberPath<Long> postId) {
        return JPAExpressions.selectOne()
                .from(postLike)
                .where(postLike.member.id.eq(memberId)
                        .and(postLike.post.id.eq(postId)))
                .exists();
    }

    private BooleanExpression isbnEq(String isbn13) {
        if (isbn13 == null) {
            return null;
        }
        return post.book.isbn13.eq(isbn13);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return post.member.id.eq(memberId);
    }

    private BooleanExpression buildPostAccessCondition(NumberPath<Long> postMemberId, Long memberId) {
        return Expressions.cases()
                .when(postMemberId.eq(memberId))
                .then(true)
                .otherwise(post.status.eq(PostStatus.PUBLIC)
                        .and(post.member.status.eq(ProfileStatus.PUBLIC)));
    }
}
