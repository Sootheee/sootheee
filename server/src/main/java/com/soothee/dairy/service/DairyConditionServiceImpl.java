package com.soothee.dairy.service;

import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NoDairyConditionException;
import com.soothee.custom.exception.NotMatchedException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyConditionServiceImpl implements DairyConditionService {
    private final DairyConditionRepository dairyConditionRepository;
    private final ConditionService conditionService;

    @Override
    public void saveConditions(List<Long> condIdList, Dairy newDairy) throws IncorrectValueException, NullValueException {
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

    @Override
    public boolean isConditionExistInDairy(Long dairyId) {
        return dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(dairyId, "N").isPresent();
    }

    @Override
    public List<Long> getConditionsIdListByDairy(Long dairyId) throws NoDairyConditionException {
        Optional<List<DairyCondition>> optional = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(dairyId, "N");
        if (optional.isPresent()) {
            List<DairyCondition> conditions = optional.get();
            List<Long> conditionIdList = new ArrayList<>();
            for (DairyCondition dairyCondition : conditions) {
                conditionIdList.add(dairyCondition.getCondition().getCondId());
            }
            return conditionIdList;
        }
        throw new NoDairyConditionException(dairyId);
    }

    @Override
    public void updateConditions(Dairy curDairy, List<Long> inputCondIds) throws NotMatchedException, IncorrectValueException, NullValueException {
        /* 업데이트할 일기의 현재 컨디션 리스트 */
        List<DairyCondition> curList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(curDairy.getDairyId(), "N")
                .orElse(new ArrayList<>());
        int curSize = curList.size();
        int inputSize = inputCondIds.size();
        /* 현재 컨디션과 입력한 컨디션 비교 */
        for (int i = 0; i < Math.min(curSize, inputSize); i++){
            DairyCondition curCondition = curList.get(i);
            /* 현재 컨디션 리스트의 일기와 업데이트할 일기가 일치하지 않으면 Exception 발생 */
            if (!Objects.equals(curCondition.getDairy().getDairyId(), curDairy.getDairyId())) {
                throw new NotMatchedException(curDairy, curCondition);
            }
            /* 1. 현재 컨디션과 입력한 컨디션이 일치하지 않거나
             * 2. 현재 컨디션의 순서와 입력한 컨디션의 순서가 일치하지 않으면
             * -> 현재 컨디션을 소프트 삭제하고 새 컨디션을 저장 */
            if (!Objects.equals(curCondition.getCondition().getCondId(), inputCondIds.get(i))
                    || !Objects.equals(curCondition.getOrderNo(), i)) {
                /* 일기의 컨디션을 업데이트 하기 위해 기존의 컨디션은 소프트 삭제 처리 */
                curCondition.deleteDairyCondition();
                /* 새 일기-컨디션 저장 */
                this.saveNewDairyCondition(curDairy, inputCondIds.get(i), i);
            }
        }
        /* 현재 컨디션 갯수가 입력된 컨디션 갯수보다 많으면 -> 입력된 컨대션 갯수보다 많은 현재 컨디션은 삭제됨 */
        if (curSize > inputSize) {
            for (int i = inputSize; i < curSize; i++) {
                curList.get(i).deleteDairyCondition();
            }
        }
        /* 현재 컨디션 갯수보다 입력된 컨디션 갯수가 많으면 -> 초과로 입력된 컨디션 저장 */
        if (curSize < inputSize) {
            for (int i = curSize; i < inputSize; i++) {
                /* 새 일기-컨디션 저장 */
                this.saveNewDairyCondition(curDairy, inputCondIds.get(i), i);
            }
        }
    }

    @Override
    public void deleteDairyConditionsOfDairy(Dairy dairy) throws NoDairyConditionException {
        Optional<List<DairyCondition>> optional = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(dairy.getDairyId(), "N");
        if (optional.isPresent()) {
            List<DairyCondition> curList = optional.get();
            for (DairyCondition dairyCondition : curList) {
                dairyCondition.deleteDairyCondition();
            }
        }
        throw new NoDairyConditionException(dairy.getDairyId());
    }

    /**
     * 새 일기-컨디션 저장
     *
     * @param dairy 해당 일기
     * @param condId 해당 컨디션 일련번호
     * @param idx 일기-컨디션 순서번호
     */
    private void saveNewDairyCondition(Dairy dairy, Long condId, int idx) throws NullValueException, IncorrectValueException {
        Condition inputCond = conditionService.getConditionById(condId);
        /* 일기의 새 일기-컨디션 생성 */
        DairyCondition newDairyCondition = DairyCondition.builder()
                                                        .dairy(dairy)
                                                        .condition(inputCond)
                                                        .orderNo(idx)
                                                        .build();
        /* 새 일기-컨디션 저장 */
        dairyConditionRepository.save(newDairyCondition);
    }
}
