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
        for (Long condId : condIdList) {
            Condition condition = conditionService.getConditionById(condId);
            DairyCondition dairyCondition = DairyCondition.of(newDairy, condition);
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
        List<DairyCondition> findList = dairyConditionRepository.findByDairyDairyIdAndIsDelete(dairyId, "N")
                .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
        List<Long> conditionIdList = new ArrayList<>();
        for (DairyCondition dairyCondition : findList) {
            conditionIdList.add(dairyCondition.getCondition().getCondId());
        }
        return conditionIdList;
    }
}
