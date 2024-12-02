package com.soothee.dairy.service;

import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
