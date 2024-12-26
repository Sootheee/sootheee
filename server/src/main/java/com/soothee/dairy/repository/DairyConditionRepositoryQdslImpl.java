package com.soothee.dairy.repository;

import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.BooleanYN;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.dairy.domain.QDairyCondition;
import com.soothee.stats.dto.ConditionRatio;
import com.soothee.stats.dto.QConditionRatio;
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
    public Optional<List<ConditionRatio>> findConditionRatioListInMonth(Long memberId, MonthParam monthParam, Integer limit, Integer count)   {
        return Optional.ofNullable(
                queryFactory.select(new QConditionRatio(dairyCondition.condition.condId,
                                                            MathExpressions.round(
                                                                    dairyCondition.condition.condId.count().doubleValue().divide(count.doubleValue())
                                                                    , 3).multiply(100)))
                            .from(dairyCondition)
                            .where(dairyCondition.dairy.member.memberId.eq(memberId),
                                    dairyCondition.dairy.date.year().eq(monthParam.getYear()),
                                    dairyCondition.dairy.date.month().eq(monthParam.getMonth()),
                                    dairyCondition.dairy.isDelete.eq(BooleanYN.N.toString()),
                                    dairyCondition.isDelete.eq(BooleanYN.N.toString()))
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
    public Optional<Integer> getAllDairyConditionCntInMonth(Long memberId, MonthParam monthParam)   {
        return Optional.ofNullable(
                queryFactory.select(dairyCondition.count().intValue())
                        .from(dairyCondition)
                        .where(dairyCondition.dairy.member.memberId.eq(memberId),
                                dairyCondition.dairy.date.year().eq(monthParam.getYear()),
                                dairyCondition.dairy.date.month().eq(monthParam.getMonth()),
                                dairyCondition.dairy.isDelete.eq(BooleanYN.N.toString()),
                                dairyCondition.isDelete.eq(BooleanYN.N.toString()))
                        .fetchOne()
        );
    }
}
