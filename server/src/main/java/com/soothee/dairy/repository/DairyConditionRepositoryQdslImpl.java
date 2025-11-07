package com.soothee.dairy.repository;

import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.BooleanYN;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.domain.QDairyCondition;
import com.soothee.stats.controller.response.ConditionRatio;
import com.soothee.stats.controller.response.QConditionRatio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DairyConditionRepositoryQdslImpl implements DairyConditionRepositoryQdsl{
    private final JPAQueryFactory queryFactory;
    private final QDairyCondition dairyCondition = QDairyCondition.dairyCondition;

    @Override
    public Optional<List<DairyCondition>> findDairyConditionListByDairyId(Long dairyId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(dairyCondition)
                        .where(dairyCondition.dairy.dairyId.eq(dairyId),
                                dairyCondition.dairy.isDelete.eq(BooleanYN.N),
                                dairyCondition.isDelete.eq(BooleanYN.N))
                        .orderBy(dairyCondition.orderNo.asc())
                        .fetch()
        );
    }

    @Override
    public Optional<List<String>> findConditionIdListByDairyId(Long dairyId) {
        return Optional.ofNullable(
                queryFactory.select(dairyCondition.condition.condId)
                        .from(dairyCondition)
                        .where(dairyCondition.dairy.dairyId.eq(dairyId),
                                dairyCondition.dairy.isDelete.eq(BooleanYN.N),
                                dairyCondition.isDelete.eq(BooleanYN.N))
                        .orderBy(dairyCondition.orderNo.asc())
                        .fetch()
        );
    }

    @Override
    public Optional<List<ConditionRatio>> findConditionRatioInMonth(Long memberId, Integer year, Integer month, Integer limit, Integer count)   {
        return Optional.ofNullable(
                queryFactory.select(new QConditionRatio(dairyCondition.condition.condId,
                                                            MathExpressions.round(
                                                                    dairyCondition.condition.condId.count().doubleValue().divide(count.doubleValue())
                                                                    , 3).multiply(100)))
                            .from(dairyCondition)
                            .where(dairyCondition.dairy.member.memberId.eq(memberId),
                                    dairyCondition.dairy.date.year().eq(year),
                                    dairyCondition.dairy.date.month().eq(month),
                                    dairyCondition.dairy.isDelete.eq(BooleanYN.N),
                                    dairyCondition.isDelete.eq(BooleanYN.N))
                            .groupBy(dairyCondition.condition.condId,
                                        dairyCondition.condition.condType.condTypeValue,
                                        dairyCondition.condition.condValue)
                            .orderBy(dairyCondition.condition.condId.count().intValue().desc(),
                                        dairyCondition.orderNo.min().asc(),
                                        dairyCondition.condition.condType.condTypeValue.desc(),
                                        dairyCondition.condition.condValue.desc())
                            .limit(limit)
                            .fetch()
        );
    }

    @Override
    public Optional<Integer> getSelectedConditionsCountInMonth(Long memberId, Integer year, Integer month)   {
        return Optional.ofNullable(
                queryFactory.select(dairyCondition.count().intValue())
                        .from(dairyCondition)
                        .where(dairyCondition.dairy.member.memberId.eq(memberId),
                                dairyCondition.dairy.date.year().eq(year),
                                dairyCondition.dairy.date.month().eq(month),
                                dairyCondition.dairy.isDelete.eq(BooleanYN.N),
                                dairyCondition.isDelete.eq(BooleanYN.N))
                        .fetchOne()
        );
    }
}
