package com.soothee.dairy.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.dairy.controller.response.DairyAllResponse;
import com.soothee.dairy.controller.response.QDairyAllResponse;
import com.soothee.dairy.controller.response.QDairyScoresResponse;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.controller.response.DairyScoresResponse;
import com.soothee.stats.controller.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DairyRepositoryQdslImpl implements DairyRepositoryQdsl {
    private final JPAQueryFactory queryFactory;
    private final QDairy dairy = QDairy.dairy;

    @Override
    public Optional<List<DairyScoresResponse>> findScoreListInMonth(Long memberId, Integer year, Integer month)   {
        return Optional.of(
                queryFactory.select(new QDairyScoresResponse(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<DairyAllResponse>> findAllDairyInfoByDate(Long memberId, LocalDate date)   {
        return Optional.ofNullable(
                queryFactory.select(new QDairyAllResponse(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.weather.weatherId,
                                                    dairy.score,
                                                    dairy.content,
                                                    dairy.hope,
                                                    dairy.thank,
                                                    dairy.learn))
                            .from(dairy)
                            .groupBy(dairy.dairyId)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.eq(date),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<DairyAllResponse>> findAllDairyInfoByDiaryId(Long memberId, Long dairyId)   {
        return Optional.ofNullable(
                queryFactory.select(new QDairyAllResponse(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.weather.weatherId,
                                                    dairy.score,
                                                    dairy.content,
                                                    dairy.hope,
                                                    dairy.thank,
                                                    dairy.learn))
                            .from(dairy)
                            .groupBy(dairy.dairyId)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.dairyId.eq(dairyId),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<Dairy>> findDairyByDairyId(Long dairyId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(dairy)
                        .where(dairy.dairyId.eq(dairyId),
                                dairy.isDelete.eq(BooleanYN.N))
                        .fetch()
        );
    }

    @Override
    public Optional<List<MonthlyDairyStats>> findDairyStatsInMonth(Long memberId, Integer year, Integer month)   {
        return Optional.ofNullable(
                queryFactory.select(new QMonthlyDairyStats(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    private BooleanExpression getContentTypeNull(ContentType type) {
        return Objects.equals(type, ContentType.THANKS) ? dairy.thank.isNotNull() : dairy.learn.isNotNull();
    }

    private BooleanExpression getContentTypeEmpty(ContentType type) {
        return Objects.equals(type, ContentType.THANKS) ? dairy.thank.isNotEmpty() : dairy.learn.isNotEmpty();
    }

    @Override
    public Optional<Integer> getMonthlyContentsCount(Long memberId, ContentType type, Integer year, Integer month) {
        return Optional.ofNullable(
                queryFactory.select(dairy.dairyId.count().intValue())
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetchOne()
        );
    }

    private Expression<String> getContentType(ContentType type) {
        return Objects.equals(type, ContentType.THANKS) ? dairy.thank : dairy.learn;
    }

    private OrderSpecifier<Double> getContentHighLow(SortType orderBy) {
        return Objects.equals(orderBy, SortType.HIGH) ? dairy.score.desc() : dairy.score.asc();
    }

    @Override
    public Optional<List<DateContents>> findOneContentByHighestOrLowestScoreInMonth(Long memberId, ContentType type, Integer year, Integer month, SortType orderBy)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateContents(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score,
                                                        getContentType(type)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .orderBy(getContentHighLow(orderBy))
                            .limit(1)
                            .fetch()
        );
    }

    @Override
    public Optional<List<WeeklyDairyStats>> findDairyStatsInWeek(Long memberId, Integer year, Integer week)   {
        return Optional.ofNullable(
                queryFactory.select(new QWeeklyDairyStats(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.week().eq(week),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<DateScore>> findDiaryScoreInWeek(Long memberId, Integer year, Integer week)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateScore(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.week().eq(week),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .orderBy(dairy.date.dayOfYear().asc())
                            .fetch()
        );
    }

    private OrderSpecifier<?> getOrderBy(SortType orderBy) {
        return Objects.equals(orderBy, SortType.DATE) ? dairy.date.asc()
                : Objects.equals(orderBy, SortType.HIGH) ? dairy.score.desc() : dairy.score.asc();
    }

    @Override
    public Optional<List<DateContents>> findSortedContentDetailInMonth(Long memberId, ContentType type, Integer year, Integer month, SortType orderBy)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateContents(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score,
                                                        getContentType(type)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .orderBy(getOrderBy(orderBy))
                            .fetch()
        );
    }
}
