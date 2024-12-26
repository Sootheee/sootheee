package com.soothee.dairy.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.*;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DairyConditionServiceImpl implements DairyConditionService {
    private final DairyConditionRepository dairyConditionRepository;
    private final ConditionService conditionService;

    @Override
    public void saveConditions(List<String> condIdList, Dairy newDairy) throws NullValueException, IncorrectValueException {
        for (int i = 0; i < condIdList.size(); i++) {
            /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            SootheeValidation.checkReferenceId(condIdList.get(i), ReferenceType.CONDITION);
            saveNewDairyCondition(newDairy, condIdList.get(i), i);
        }
    }

    @Override
    public boolean isConditionExistInDairy(Long dairyId) {
        return dairyConditionRepository.findByDairyDairyIdAndDairyIsDeleteAndIsDeleteOrderByOrderNoAsc(dairyId, BooleanYN.N.toString(), BooleanYN.N.toString()).isPresent();
    }

    @Override
    public List<String> getConditionsIdListByDairy(Long dairyId) throws NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생 */
        List<DairyCondition> conditions = getDairyConditionListByDairyId(dairyId);

        List<String> conditionIdList = new ArrayList<>();
        for (DairyCondition dairyCondition : conditions) {
            conditionIdList.add(dairyCondition.getCondition().getCondId());
        }
        return conditionIdList;
    }

    @Override
    public void updateConditions(Dairy curDairy, List<String> inputCondIds) throws NotMatchedException, IncorrectValueException, NullValueException, NotFoundDetailInfoException {
        for (String condId : inputCondIds) {
            /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            SootheeValidation.checkReferenceId(condId, ReferenceType.CONDITION);
        }
        /* 업데이트할 일기의 현재 컨디션 리스트 - 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생 */
        List<DairyCondition> curList = getDairyConditionListByDairyId(curDairy.getDairyId());

        int curSize = curList.size();
        int inputSize = inputCondIds.size();

        /* 현재 컨디션과 입력한 컨디션 비교 */
        for (int i = 0; i < Math.min(curSize, inputSize); i++){
            DairyCondition curCondition = curList.get(i);
            /* 현재 컨디션 리스트의 일기와 업데이트할 일기가 일치하지 않으면 Exception 발생 */
            SootheeValidation.checkMatchedId(curCondition.getDairy().getDairyId(), curDairy.getDairyId(), DomainType.DAIRY);
            /* 1. 현재 컨디션과 입력한 컨디션이 일치하지 않거나
             * 2. 현재 컨디션의 순서와 입력한 컨디션의 순서가 일치하지 않으면
             * -> 현재 등록된 미일치 일기-컨디션을 소프트 삭제하고 새 일기-컨디션을 등록 */
            if (checkNotMatchedCondId(curCondition.getCondition().getCondId(), inputCondIds.get(i))
                    || checkNotMatchedOrderNo(curCondition.getOrderNo(), i)) {
                /* 일기의 컨디션을 업데이트 하기 위해 기존의 미일치 일기-컨디션은 소프트 삭제 처리 */
                curCondition.deleteDairyCondition();
                /* 새 일기-컨디션 등록 */
                saveNewDairyCondition(curDairy, inputCondIds.get(i), i);
            }
        }

        /* 현재 컨디션 갯수가 입력된 컨디션 갯수보다 많으면 -> 입력된 컨대션 갯수보다 많은 현재 컨디션은 삭제됨 */
        if (curSize > inputSize) {
            for (int i = inputSize; i < curSize; i++) {
                curList.get(i).deleteDairyCondition();
            }
        }
        /* 현재 컨디션 갯수보다 입력된 컨디션 갯수가 많으면 -> 초과로 입력된 새 일기-컨디션 등록 */
        if (curSize < inputSize) {
            for (int i = curSize; i < inputSize; i++) {
                /* 새 일기-컨디션 등록 */
                saveNewDairyCondition(curDairy, inputCondIds.get(i), i);
            }
        }
    }

    @Override
    public void deleteDairyConditionsOfDairy(Dairy dairy) throws NotFoundDetailInfoException, IncorrectValueException, NullValueException {
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
     * @param idx 일기-컨디션 순서번호
     */
    private void saveNewDairyCondition(Dairy dairy, String condId, int idx) throws NullValueException, IncorrectValueException {
        Condition inputCond = conditionService.getConditionById(condId);
        /* 일기의 새 일기-컨디션 생성 */
        DairyCondition newDairyCondition = DairyCondition.builder()
                                                        .dairy(dairy)
                                                        .condition(inputCond)
                                                        .orderNo(idx)
                                                        .build();
        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        newDairyCondition.validNew();
        /* 새 일기-컨디션 등록 */
        dairyConditionRepository.save(newDairyCondition);
    }

    /**
     * 기존 컨디션 일련번호와 입력된 컨디션 일련번호가 일치하지 않는지 검증
     *
     * @param curId 기존 일련번호
     * @param inputId 입력한 일련번호
     * @return 다르면 true/같으면 false
     */
    private boolean checkNotMatchedCondId(String curId, String inputId) {
        /* 기존 일기에 선택한 컨디션 일련번호와 입력된 컨디션 일련번호가 일치하는지 확인 */
        return !StringUtils.equals(curId, inputId);
    }

    /**
     * 기존 컨디션 입력 순서와 입력된 컨디션 입력 순서가 일치하지 않는지 검증
     *
     * @param curNo 기존 입력 순서
     * @param inputNo 입력한 입력 순서
     * @return 다르면 true/같으면 false
     */
    private boolean checkNotMatchedOrderNo(int curNo, int inputNo) {
        return curNo != inputNo;
    }

    /**
     * 일기 일련번호로 해당 일기에서 선택한 모든 컨디션 정보 리스트 조회
     * - 삭제한 일기 제외
     * 1. 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 선택하 모든 컨디션 정보 리스트
     */
    private List<DairyCondition> getDairyConditionListByDairyId(Long dairyId) throws NotFoundDetailInfoException, IncorrectValueException, NullValueException {
        List<DairyCondition> result = dairyConditionRepository.findByDairyDairyIdAndDairyIsDeleteAndIsDeleteOrderByOrderNoAsc(dairyId, BooleanYN.N.toString(), BooleanYN.N.toString())
                /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 Exception 발생 */
                .orElseThrow(() -> new NotFoundDetailInfoException(DomainType.DAIRY_CONDITION));
        for (DairyCondition dairyCondition : result) {
            dairyCondition.valid();
        }
        return result;
    }
}
