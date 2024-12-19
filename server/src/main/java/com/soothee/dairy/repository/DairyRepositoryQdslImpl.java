package com.soothee.dairy.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DairyRepositoryQdslImpl implements DairyRepositoryQdsl {
    private final JPAQueryFactory queryFactory;
    private final QDairy dairy = QDairy.dairy;

    @Override
    public Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId, MonthParam monthParam) {
        return Optional.of(
                queryFactory.select(new QDairyScoresDTO(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    dairy.isDelete.eq("N"))
                            .fetch()
        );
    }

    @Override
    public Optional<DairyDTO> findByDate(Long memberId, LocalDate date) {
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
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    @Override
    public Optional<DairyDTO> findByMemberDiaryId(Long memberId, Long dairyId) {
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
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    @Override
    public Optional<MonthlyStatsDTO> findDiaryStatsInMonth(Long memberId, MonthParam monthParam) {
        return Optional.ofNullable(
                queryFactory.select(new QMonthlyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    private BooleanExpression getContentTypeNull(String type) {
        return StringUtils.equals(type, ContentType.THANKS.toString()) ? dairy.thank.isNotNull() : dairy.learn.isNotNull();
    }

    private BooleanExpression getContentTypeEmpty(String type) {
        return StringUtils.equals(type, ContentType.THANKS.toString()) ? dairy.thank.isNotEmpty() : dairy.learn.isNotEmpty();
    }

    @Override
    public Optional<Integer> findDiaryContentCntInMonth(Long memberId, String type, MonthParam monthParam) {
        return Optional.ofNullable(
                queryFactory.select(dairy.dairyId.count().intValue())
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(monthParam.getYear()),
                                    dairy.date.month().eq(monthParam.getMonth()),
                                    getContentTypeNull(type),
                                    getContentTypeEmpty(type),
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    private Expression<String> getContentType(String type) {
        return StringUtils.equals(type, ContentType.THANKS.toString()) ? dairy.thank : dairy.learn;
    }

    private OrderSpecifier<Double> getContentHighLow(String high) {
        return StringUtils.equals(high, SortType.HIGH.toString()) ? dairy.score.desc() : dairy.score.asc();
    }

    @Override
    public Optional<DateContents> findDiaryContentInMonthHL(Long memberId, String type, MonthParam monthParam, String high) {
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
                                    dairy.isDelete.eq("N"))
                            .orderBy(getContentHighLow(high))
                            .limit(1)
                            .fetchOne()
        );
    }

    @Override
    public Optional<WeeklyStatsDTO> findDiaryStatsInWeekly(Long memberId, WeekParam weekParam) {
        return Optional.ofNullable(
                queryFactory.select(new QWeeklyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(weekParam.getYear()),
                                    dairy.date.week().eq(weekParam.getWeek()),
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    @Override
    public Optional<List<DateScore>> findDiaryScoresInWeekly(Long memberId, WeekParam weekParam) {
        return Optional.ofNullable(
                queryFactory.select(new QDateScore(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(weekParam.getYear()),
                                    dairy.date.week().eq(weekParam.getWeek()),
                                    dairy.isDelete.eq("N"))
                            .orderBy(dairy.date.dayOfYear().asc())
                            .fetch()
        );
    }

    private OrderSpecifier<?> getOrderBy(String orderBy) {
        return StringUtils.equals(orderBy, SortType.DATE.toString()) ? dairy.date.asc()
                : StringUtils.equals(orderBy, SortType.HIGH.toString()) ? dairy.score.desc() : dairy.score.asc();
    }

    @Override
    public Optional<List<DateContents>> findDiaryContentInMonthSort(Long memberId, String type, MonthParam monthParam, String orderBy) {
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
                                    dairy.isDelete.eq("N"))
                            .orderBy(getOrderBy(orderBy))
                            .fetch()
        );
    }
}
