package com.soothee.dairy.service;

import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NoDairyConditionException;
import com.soothee.custom.exception.NotMatchedException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.dairy.domain.Dairy;

import java.util.List;

public interface DairyConditionService {
    /**
     * 해당 일기에서 선택한 컨디션들 저장
     *
     * @param condIdList 선택한 컨디션들의 일련번호 리스트
     * @param newDairy 해당 일기
     */
    void saveConditions(List<Long> condIdList, Dairy newDairy) throws IncorrectValueException, NullValueException;

    boolean isConditionExistInDairy(Long dairyId);

    /**
     * 해당 일기의 일기-컨디션 리스트 조회
     * 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 해당 일기의 다수의 컨디션 일련번호 리스트
     */
    List<Long> getConditionsIdListByDairy(Long dairyId) throws NoDairyConditionException;

    /**
     * 해당 일기의 일기-컨디션 리스트 업데이트
     *
     * @param curDairy 조회할 일기 일련번호
     * @param inputCondIds 업데이트될 컨디션 리스트
     */
    void updateConditions(Dairy curDairy, List<Long> inputCondIds) throws NotMatchedException, IncorrectValueException, NullValueException;

    /**
     * 해당 일기의 일기-컨디션 리스트 모두 소프트삭제
     *
     * @param dairy 삭제할 일기 일련번호
     */
    void deleteDairyConditionsOfDairy(Dairy dairy) throws NoDairyConditionException;
}
