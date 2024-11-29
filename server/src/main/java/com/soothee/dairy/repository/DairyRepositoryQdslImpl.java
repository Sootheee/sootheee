package com.soothee.dairy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.dairy.domain.QDairy;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.dto.QDairyScoresDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DairyRepositoryQdslImpl implements DairyRepositoryQdsl {
    private final JPAQueryFactory queryFactory;
    private final QDairy dairy = QDairy.dairy;

    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회</hr>
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year Integer : 지정한 년도
     * @param month Integer : 지정한 월
     * @return Optional<List<DairyScoresDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    public Optional<List<DairyScoresDTO>> findByMemberIdAndDate(@Param("member_id") Long memberId,
                                                         @Param("year") Integer year,
                                                         @Param("month") Integer month) {
        return Optional.of(queryFactory.select(new QDairyScoresDTO(dairy.dairyId, dairy.date, dairy.score))
                .from(dairy)
                .where(dairy.member.memberId.eq(memberId),
                        dairy.date.year().eq(year),
                        dairy.date.month().eq(month)).fetch());
    }
}
