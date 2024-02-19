package cotato.bookitlist.review.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.bookitlist.member.domain.ProfileStatus;
import cotato.bookitlist.review.domain.ReviewStatus;
import cotato.bookitlist.review.dto.ReviewDetailDto;
import cotato.bookitlist.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static cotato.bookitlist.member.domain.QMember.member;
import static cotato.bookitlist.review.domain.entity.QReview.review;
import static cotato.bookitlist.review.domain.entity.QReviewLike.reviewLike;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewDto> findPublicReviewWithLikedByIsbn13(String isbn13, Long memberId, Long loginMemberId, Pageable pageable) {
        List<ReviewDto> result = queryFactory
                .select(
                        Projections.constructor(
                                ReviewDto.class,
                                review.id,
                                review.member.id,
                                review.book.id,
                                review.content,
                                review.likeCount,
                                review.viewCount,
                                Expressions.cases()
                                        .when(isLikedByMember(loginMemberId, review.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked"),
                                review.status
                        )
                )
                .from(review)
                .join(review.member, member)
                .where(isbnEq(isbn13), memberIdEq(memberId), review.status.eq(ReviewStatus.PUBLIC), review.member.status.eq(ProfileStatus.PUBLIC))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(isbnEq(isbn13), memberIdEq(memberId))
                .from(review);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ReviewDetailDto> findPublicReviewDetailByReviewId(Long reviewId, Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(
                        Projections.constructor(
                                ReviewDetailDto.class,
                                review.id,
                                review.member.id,
                                review.book.id,
                                review.content,
                                review.likeCount,
                                review.viewCount,
                                review.createdAt,
                                review.modifiedAt,
                                Expressions.cases()
                                        .when(isLikedByMember(memberId, review.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked"),
                                review.status
                        )
                )
                .from(review)
                .join(review.member, member)
                .where(review.id.eq(reviewId), buildReviewAccessCondition(review.member.id, memberId))
                .fetchOne());
    }

    private BooleanExpression isLikedByMember(Long memberId, NumberPath<Long> reviewId) {
        return JPAExpressions.selectOne()
                .from(reviewLike)
                .where(reviewLike.member.id.eq(memberId)
                        .and(reviewLike.review.id.eq(reviewId)))
                .exists();
    }

    private BooleanExpression isbnEq(String isbn13) {
        if (isbn13 == null) {
            return null;
        }
        return review.book.isbn13.eq(isbn13);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return review.member.id.eq(memberId);
    }

    private BooleanExpression buildReviewAccessCondition(NumberPath<Long> reviewMemberId, Long memberId) {
        return Expressions.cases()
                .when(reviewMemberId.eq(memberId))
                .then(true)
                .otherwise(review.status.eq(ReviewStatus.PUBLIC)
                        .and(review.member.status.eq(ProfileStatus.PUBLIC)));
    }
}
