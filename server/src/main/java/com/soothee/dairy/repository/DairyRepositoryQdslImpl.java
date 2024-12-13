package com.soothee.dairy.repository;

import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.QDairyDTO;
import com.soothee.dairy.dto.QDairyScoresDTO;
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.stats.dto.QMonthlyStatsDTO;
import com.soothee.stats.dto.QWeeklyStatsDTO;
import com.soothee.stats.dto.WeeklyStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class DairyRepositoryQdslImpl implements DairyRepositoryQdsl {
    private final JPAQueryFactory queryFactory;
    private final QDairy dairy = QDairy.dairy;

    @Override
    public Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId, Integer year, Integer month) {
        return Optional.of(
                queryFactory.select(new QDairyScoresDTO(dairy.dairyId,
                                                        dairy.date,
                                                        dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
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
    public Optional<DairyDTO> findByDiaryId(Long memberId, Long dairyId) {
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
    public Optional<MonthlyStatsDTO> findDiaryStatsInMonth(Long memberId, Integer year, Integer month) {
        return Optional.ofNullable(
                queryFactory.select(new QMonthlyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    @Override
    public Optional<WeeklyStatsDTO> findDiaryStatsInWeekly(Long memberId, Integer year, Integer week) {
        return Optional.ofNullable(
                queryFactory.select(new QWeeklyStatsDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.week().eq(week),
                                    dairy.isDelete.eq("N"))
                            .fetchOne()
        );
    }

    @Override
    public Optional<List<DateScore>> findDiaryScoresInWeekly(Long memberId, Integer year, Integer week) {
        return Optional.ofNullable(
                queryFactory.select(new QDateScore(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.score))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.week().eq(week),
                                    dairy.isDelete.eq("N"))
                            .orderBy(dairy.date.dayOfYear().asc())
                            .fetch()
        );
    }
}
