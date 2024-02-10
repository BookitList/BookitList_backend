package cotato.bookitlist.review.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.bookitlist.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static cotato.bookitlist.review.domain.QReview.review;
import static cotato.bookitlist.review.domain.QReviewLike.reviewLike;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<ReviewDto> findReviewByReviewId(Long reviewId, Long memberId) {
        return Optional.ofNullable(queryFactory
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
                                        .when(isLikedByMember(memberId, review.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("liked")
                        )
                )
                .from(review)
                .where(review.id.eq(reviewId))
                .fetchOne());
    }

    private BooleanExpression isLikedByMember(Long memberId, NumberPath<Long> reviewId) {
        return JPAExpressions.selectOne()
                .from(reviewLike)
                .where(reviewLike.member.id.eq(memberId)
                        .and(reviewLike.review.id.eq(reviewId)))
                .exists();
    }
}
