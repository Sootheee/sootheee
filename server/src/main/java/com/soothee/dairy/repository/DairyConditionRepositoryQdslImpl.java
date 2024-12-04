package com.soothee.dairy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.dairy.domain.QDairyCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DairyConditionRepositoryQdslImpl implements DairyConditionRepositoryQdsl{
    private final JPAQueryFactory queryFactory;
    private final QDairyCondition dairyCondition = QDairyCondition.dairyCondition;

    @Override
    public Optional<Long> findMostOneCondIdInMonth(Long memberId, Integer year, Integer month) {
        return Optional.ofNullable(queryFactory.select(dairyCondition.condition.condId)
                .from(dairyCondition)
                .where(dairyCondition.dairy.member.memberId.eq(memberId),
                        dairyCondition.dairy.date.year().eq(year),
                        dairyCondition.dairy.date.month().eq(month),
                        dairyCondition.isDelete.eq("N"))
                .groupBy(dairyCondition.condition.condId)
                .orderBy(dairyCondition.condition.condId.count().intValue().desc())
                .limit(1)
                .fetchOne());
    }
}
