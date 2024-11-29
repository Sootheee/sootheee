package com.soothee.dairy.service;

import com.soothee.dairy.domain.Dairy;

import java.util.List;

public interface DairyConditionService {
    /**
     * 해당 일기에서 선택한 컨디션들 저장
     *
     * @param condIdList List<Long> : 선택한 컨디션들의 일련번호 리스트
     * @param newDairy Dairy : 해당 일기
     */
    void saveConditions(List<Long> condIdList, Dairy newDairy);
}
