package com.soothee.dairy.service;

import com.soothee.dairy.domain.Dairy;

import java.util.List;

public interface DairyConditionService {
    /**
     * 해당 일기에서 선택한 컨디션들 저장</hr>
     *
     * @param condIdList List<Long> : 선택한 컨디션들의 일련번호 리스트
     * @param newDairy Dairy : 해당 일기
     */
    void saveConditions(List<Long> condIdList, Dairy newDairy);

    /**
     * 해당 일기의 컨디션 리스트 조회</hr>
     * 삭제한 컨디션 제외
     *
     * @param dairyId Long : 조회할 일기 일련번호
     * @return List<Long> : 해당 일기의 다수의 컨디션 일련번호 리스트
     */
    List<Long> getConditionsIdListByDairy(Long dairyId);

    /**
     * 해당 일기의 컨디션 리스트 업데이트</hr>
     *
     * @param curDairy Dairy : 조회할 일기 일련번호
     * @param inputCondIds List<Long> : 업데이트될 컨디션 리스트
     */
    void updateConditions(Dairy curDairy, List<Long> inputCondIds);
}
