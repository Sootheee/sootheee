package com.soothee.dairy.repository;

import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.QDairyDTO;
import com.soothee.dairy.dto.QDairyScoresDTO;
import com.soothee.stats.dto.MonthlyAvgDTO;
import com.soothee.stats.dto.QMonthlyAvgDTO;
import lombok.RequiredArgsConstructor;
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
    public MonthlyAvgDTO summaryDairiesInMonth(Long memberId, Integer year, Integer month) {
        return queryFactory.select(new QMonthlyAvgDTO(dairy.dairyId.count().intValue(),
                                                        MathExpressions.round(dairy.score.avg(), 2)))
                            .from(dairy)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.year().eq(year),
                                    dairy.date.month().eq(month),
                                    dairy.isDelete.eq("N")).fetchOne();
    }
}
