package com.soothee.dairy.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyConditionServiceImpl implements DairyConditionService{
    private final DairyConditionRepository dairyConditionRepository;
    private final ConditionService conditionService;

    /**
     * 해당 일기에서 선택한 컨디션들 저장</hr>
     *
     * @param condIdList List<Long> : 선택한 컨디션들의 일련번호 리스트
     * @param newDairy   Dairy : 해당 일기
     */
    @Override
    public void saveConditions(List<Long> condIdList, Dairy newDairy) {
        int idx = 0;
        for (Long condId : condIdList) {
            Condition condition = conditionService.getConditionById(condId);
            DairyCondition dairyCondition = DairyCondition.builder()
                                                        .dairy(newDairy)
                                                        .condition(condition)
                                                        .orderNo(idx++)
                                                        .build();
            dairyConditionRepository.save(dairyCondition);
        }
    }

    /**
     * 해당 일기의 컨디션 리스트 조회</hr>
     * 삭제한 컨디션 제외
     *
     * @param dairyId Long : 조회할 일기 일련번호
     * @return List<Long> : 해당 일기의 다수의 컨디션 일련번호 리스트
     */
    @Override
    public List<Long> getConditionsIdListByDairy(Long dairyId) {
        List<DairyCondition> findList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(dairyId, "N")
                .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
        List<Long> conditionIdList = new ArrayList<>();
        for (DairyCondition dairyCondition : findList) {
            conditionIdList.add(dairyCondition.getCondition().getCondId());
        }
        return conditionIdList;
    }

    /**
     * 해당 일기의 컨디션 리스트 업데이트</hr>
     *
     * @param curDairy Dairy : 조회할 일기 일련번호
     * @param inputCondIds List<Long> : 업데이트될 컨디션 리스트
     */
    @Override
    public void updateConditions(Dairy curDairy, List<Long> inputCondIds) {
        /* 업데이트할 일기의 현재 컨디션 리스트 */
        List<DairyCondition> curList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(curDairy.getDairyId(), "N")
                .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
        int idx = 0;
        for (DairyCondition curCond : curList) {
            /* 현재 컨디션 리스트의 일기와 업데이트할 일기가 일치하지 않으면 Exception 발생 */
            if (!Objects.equals(curCond.getDairy().getDairyId(), curDairy.getDairyId())) {
                throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
            }
            /* 1. 현재 컨디션과 입력한 컨디션이 일치하고
             * 2. 현재 컨디션의 순서와 입력한 컨디션의 순서가 일치하면 업데이트 하지 않음 */
            if (Objects.equals(curCond.getCondition().getCondId(), inputCondIds.get((idx)))
                && Objects.equals(curCond.getOrderNo(), idx)) {
                continue;
            }
            /* 일기의 컨디션을 업데이트 하기 위해 기존의 컨디션은 소프트 삭제 처리 */
            curCond.deleteDairyCondition();
            Condition inputCond = conditionService.getConditionById(inputCondIds.get(idx));
            /* 업데이트할 새 일기의 컨디션 생성 */
            DairyCondition newDairyCondition = DairyCondition.builder()
                                                                .dairy(curDairy)
                                                                .condition(inputCond)
                                                                .orderNo(idx++)
                                                                .build();
            /* 새 일기 컨디션 저장 */
            dairyConditionRepository.save(newDairyCondition);
        }
    }
}
