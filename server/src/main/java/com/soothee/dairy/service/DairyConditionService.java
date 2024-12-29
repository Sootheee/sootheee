package com.soothee.dairy.service;

import com.soothee.custom.exception.*;
import com.soothee.dairy.domain.Dairy;

import java.util.List;

public interface DairyConditionService {
    /**
     * 해당 일기에서 선택한 컨디션들로 일기-컨디션 등록
     * - 삭제한 일기 제외
     *
     * @param condIdList 선택한 컨디션들의 일련번호 리스트
     * @param newDairy 해당 일기
     */
    void saveConditions(List<String> condIdList, Dairy newDairy) throws NullValueException;

    /**
     * 해당 일기에 선택된 컨디션이 있는지 확인
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 있으면 true / 없으면 false
     */
    boolean isExistSelectedConditionsInDairy(Long dairyId);

    /**
     * 해당 일기의 일기-컨디션 리스트 조회
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @return 해당 일기의 다수의 컨디션 일련번호 리스트
     */
    List<String> getConditionsIdListByDairy(Long dairyId) throws NotFoundDairyConditionsException;

    /**
     * 해당 일기의 일기-컨디션 리스트 업데이트
     * - 삭제한 일기 제외
     *
     * @param curDairy 조회할 일기 일련번호
     * @param inputCondIds 업데이트될 컨디션 리스트
     */
    void updateConditions(Dairy curDairy, List<String> inputCondIds) throws NotFoundDairyConditionsException, NullValueException;

    /**
     * 해당 일기의 일기-컨디션 리스트 모두 소프트삭제
     * - 삭제한 일기 제외
     *
     * @param dairy 삭제할 일기 일련번호
     */
    void deleteDairyConditionsOfDairy(Dairy dairy) throws NotFoundDairyConditionsException;
}
