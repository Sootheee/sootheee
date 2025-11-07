package com.soothee.dairy.service;

import com.soothee.custom.exception.*;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyConditionServiceImpl implements DairyConditionService {
    private final DairyConditionRepository dairyConditionRepository;
    private final ConditionService conditionService;

    @Override
    public void saveConditions(List<String> condIdList, Dairy newDairy) throws NullValueException {
        for (int orderNo = 0; orderNo < condIdList.size(); orderNo++) {
            saveNewDairyCondition(newDairy, condIdList.get(orderNo), orderNo);
        }
    }

    @Override
    public boolean isExistSelectedConditionsInDairy(Long dairyId) {
        return dairyConditionRepository.findDairyConditionListByDairyId(dairyId).isPresent();
    }

    @Override
    public List<String> getConditionsIdListByDairy(Long dairyId) throws NotFoundDairyConditionsException {
        return dairyConditionRepository.findConditionIdListByDairyId(dairyId)
                .orElseThrow(() -> new NotFoundDairyConditionsException(dairyId));
    }

    @Override
    public void updateConditions(Dairy curDairy, List<String> modCondIdList) throws NotFoundDairyConditionsException, NullValueException {
        List<DairyCondition> curDairyConditionList = getDairyConditionListByDairyId(curDairy.getDairyId());

        int curListSize = curDairyConditionList.size();
        int modIdListSize = modCondIdList.size();

        for (int orderNo = 0; orderNo < Math.min(curListSize, modIdListSize); orderNo++){
            DairyCondition curCondition = curDairyConditionList.get(orderNo);
            if (isNotSameCondId(curCondition.getCondition().getCondId(), modCondIdList.get(orderNo))
                    || isNotSameOrderNo(curCondition.getOrderNo(), orderNo)) {
                curCondition.deleteDairyCondition();
                saveNewDairyCondition(curDairy, modCondIdList.get(orderNo), orderNo);
            }
        }

        if (isCurListSizeBigger(curListSize, modIdListSize)) {
            for (int i = modIdListSize; i < curListSize; i++) {
                curDairyConditionList.get(i).deleteDairyCondition();
            }
        }

        if (isModIdListSizeBigger(curListSize, modIdListSize)) {
            for (int i = curListSize; i < modIdListSize; i++) {
                saveNewDairyCondition(curDairy, modCondIdList.get(i), i);
            }
        }
    }

    @Override
    public void deleteDairyConditionsOfDairy(Dairy dairy) throws NotFoundDairyConditionsException {
        List<DairyCondition> curList = getDairyConditionListByDairyId(dairy.getDairyId());
        for (DairyCondition dairyCondition : curList) {
            dairyCondition.deleteDairyCondition();
        }
    }

    /**
     * 새 일기-컨디션 등록
     *
     * @param dairy 해당 일기
     * @param condId 해당 컨디션 일련번호
     * @param orderNo 일기-컨디션 순서번호
     */
    private void saveNewDairyCondition(Dairy dairy, String condId, int orderNo) throws NullValueException {
        Condition condition = conditionService.getConditionById(condId);
        DairyCondition newDairyCondition = DairyCondition.builder()
                                                        .dairy(dairy)
                                                        .condition(condition)
                                                        .orderNo(orderNo)
                                                        .build();
        dairyConditionRepository.save(newDairyCondition);
    }

    /**
     * 기존 컨디션 일련번호와 입력된 컨디션 일련번호가 일치하지 않는지 검증
     *
     * @param curId 기존 일련번호
     * @param inputId 입력한 일련번호
     * @return 다르면 true/같으면 false
     */
    private boolean isNotSameCondId(String curId, String inputId) {
        return !StringUtils.equals(curId, inputId);
    }

    /**
     * 기존 컨디션 입력 순서와 입력된 컨디션 입력 순서가 일치하지 않는지 검증
     *
     * @param curNo 기존 입력 순서
     * @param inputNo 입력한 입력 순서
     * @return 다르면 true/같으면 false
     */
    private boolean isNotSameOrderNo(int curNo, int inputNo) {
        return curNo != inputNo;
    }

    /**
     * 일기 일련번호로 해당 일기에서 선택한 모든 컨디션 정보 리스트 조회
     * - 삭제한 일기 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 선택하 모든 컨디션 정보 리스트
     */
    private List<DairyCondition> getDairyConditionListByDairyId(Long dairyId) throws NotFoundDairyConditionsException {
        return dairyConditionRepository.findDairyConditionListByDairyId(dairyId)
                .orElseThrow(() -> new NotFoundDairyConditionsException(dairyId));
    }


    private static boolean isModIdListSizeBigger(int curListSize, int modIdListSize) {
        return curListSize < modIdListSize;
    }

    private static boolean isCurListSizeBigger(int curListSize, int modIdListSize) {
        return curListSize > modIdListSize;
    }
}
