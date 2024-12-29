package com.soothee.dairy.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.QDairyDTO;
import com.soothee.dairy.dto.QDairyScoresDTO;
import com.soothee.stats.dto.*;
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
    public Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId, MonthParam monthParam)   {
        return Optional.of(
                queryFactory.select(new QDairyScoresDTO(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<DairyDTO>> findByDate(Long memberId, LocalDate date)   {
        return Optional.ofNullable(
                queryFactory.select(new QDairyDTO(dairy.dairyId,
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
    public Optional<List<DairyDTO>> findByMemberDiaryId(Long memberId, Long dairyId)   {
        return Optional.ofNullable(
                queryFactory.select(new QDairyDTO(dairy.dairyId,
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
    public Optional<List<MonthlyStatsDTO>> findDiaryStatsInMonth(Long memberId, MonthParam monthParam)   {
        return Optional.ofNullable(
                queryFactory.select(new QMonthlyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
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
    public Optional<Integer> findDiaryContentCntInMonth(Long memberId, ContentType type, MonthParam monthParam) {
        return Optional.ofNullable(
                queryFactory.select(dairy.dairyId.count().intValue())
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
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
    public Optional<List<DateContents>> findDiaryContentInMonthHL(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateContents(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score,
                                                        getContentType(type)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .orderBy(getContentHighLow(orderBy))
                            .limit(1)
                            .fetch()
        );
    }

    @Override
    public Optional<List<WeeklyStatsDTO>> findDiaryStatsInWeekly(Long memberId, WeekParam weekParam)   {
        return Optional.ofNullable(
                queryFactory.select(new QWeeklyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(weekParam.getYear()),
                                    dairy.date.week().eq(weekParam.getWeek()),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .fetch()
        );
    }

    @Override
    public Optional<List<DateScore>> findDiaryScoresInWeekly(Long memberId, WeekParam weekParam)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateScore(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(weekParam.getYear()),
                                    dairy.date.week().eq(weekParam.getWeek()),
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
    public Optional<List<DateContents>> findDiaryContentInMonthSort(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy)   {
        return Optional.ofNullable(
                queryFactory.select(new QDateContents(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score,
                                                        getContentType(type)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq(BooleanYN.N))
                            .orderBy(getOrderBy(orderBy))
                            .fetch()
        );
    }
}
