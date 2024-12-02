package com.soothee.dairy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.domain.QDairyCondition;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.QDairyDTO;
import com.soothee.dairy.dto.QDairyScoresDTO;
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
    private final QDairyCondition dairyCondition = QDairyCondition.dairyCondition;

    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회</hr>
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year Integer : 지정한 년도
     * @param month Integer : 지정한 월
     * @return Optional<List<DairyScoresDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
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

    /**
     * 로그인한 계정이 지정한 날에 작성한 일기 정보 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param date     LocalDate : 지정한 날짜
     * @return Optional<DairyDTO> : 조회된 일기 모든 정보 (null 가능)
     */
    @Override
    public Optional<List<DairyDTO>> findByDate(Long memberId, LocalDate date) {
        return Optional.of(
                queryFactory.select(new QDairyDTO(dairy.dairyId,
                                                    dairy.date,
                                                    dairy.weather.weatherId,
                                                    dairy.score,
                                                    dairy.content,
                                                    dairy.hope,
                                                    dairy.thank,
                                                    dairy.learn))
                            .from(dairy)
                            .leftJoin(dairyCondition).on(dairy.dairyId.eq(dairyCondition.dairy.dairyId))
                            .groupBy(dairy.dairyId)
                            .where(dairy.member.memberId.eq(memberId),
                                    dairy.date.eq(date),
                                    dairy.isDelete.eq("N"))
                                    .fetch()
        );
    }
}
